package dev.lajoscseppento.messyfiles.design.datastore.arangodb;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.GraphEntity;
import com.arangodb.model.CollectionsReadOptions;
import dev.lajoscseppento.messyfiles.design.datastore.arangodb.database.ArangoDbConnection;
import dev.lajoscseppento.messyfiles.design.datastore.arangodb.model.FileSystemEntry;
import dev.lajoscseppento.messyfiles.design.datastore.core.MockFileSystemGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

@SpringBootApplication
@Slf4j
public class ArangoDbApp implements CommandLineRunner {
  public static final String VERTEX_COLLECTION_NAME = "file-system-entries";
  public static final String GRAPH_NAME = "file-system-graph";
  public static final String EDGE_COLLECTION_NAME = "file-system-relations";

  @Autowired private ArangoDbConnection arangoDbConnection;

  @Autowired private MockFileSystemGenerator mockFileSystemGenerator;

  private ArangoDatabase database;

  public static void main(String[] args) {
    SpringApplication.run(ArangoDbApp.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {
    database = arangoDbConnection.getDatabase();

    cleanDatabase();
    generateDatabase();
    listDatabase();
  }

  private void listDatabase() {
    for (CollectionEntity collection : getNonSystemCollections()) {
      log.info(
          "Found collection {}, size: {}",
          collection.getName(),
          database.collection(collection.getName()).count().getCount());
    }

    for (GraphEntity graph : database.getGraphs()) {
      log.info("Found graph {}", graph.getName());
    }
  }

  private void generateDatabase() throws Exception {
    log.info("Creating graph...");

    ArangoGraph graph = database.graph(GRAPH_NAME);
    graph.create(Collections.emptySet());
    graph.addVertexCollection(VERTEX_COLLECTION_NAME);
    graph.addEdgeDefinition(
        new EdgeDefinition()
            .collection(EDGE_COLLECTION_NAME)
            .from(VERTEX_COLLECTION_NAME)
            .to(VERTEX_COLLECTION_NAME));

    log.info("Populating graph...");

    FileSystem fileSystem = mockFileSystemGenerator.generate();

    for (Path rootDirectory : fileSystem.getRootDirectories()) {
      Files.walkFileTree(
          rootDirectory,
          new SimpleFileVisitor<>() {
            private final Deque<FileSystemEntry> parents = new ArrayDeque<>();

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
              FileSystemEntry entry = createEntry(dir, attrs);

              if (!parents.isEmpty()) {
                createEdge(parents.peek().getId(), entry.getId());
              }

              parents.push(entry);

              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
              FileSystemEntry entry = createEntry(file, attrs);
              createEdge(parents.peek().getId(), entry.getId());
              return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
              parents.pop();
              return FileVisitResult.CONTINUE;
            }
          });
    }
  }

  private FileSystemEntry createEntry(Path path, BasicFileAttributes attrs) {
    FileSystemEntry entry = FileSystemEntry.create(path, attrs);
    database.graph(GRAPH_NAME).vertexCollection(VERTEX_COLLECTION_NAME).insertVertex(entry);
    return entry;
  }

  private void createEdge(String from, String to) {
    BaseEdgeDocument edge = new BaseEdgeDocument();
    edge.setFrom(from);
    edge.setTo(to);
    database.graph(GRAPH_NAME).edgeCollection(EDGE_COLLECTION_NAME).insertEdge(edge);
  }

  private void cleanDatabase() {
    for (CollectionEntity collection : getNonSystemCollections()) {
      log.info("Deleting collection {}", collection.getName());
      database.collection(collection.getName()).drop();
    }

    for (GraphEntity graph : database.getGraphs()) {
      log.info("Deleting graph {}", graph.getName());
      database.graph(graph.getName()).drop();
    }
  }

  private Collection<CollectionEntity> getNonSystemCollections() {
    CollectionsReadOptions options = new CollectionsReadOptions();
    options.excludeSystem(true);
    return database.getCollections(options);
  }
}
