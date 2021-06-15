package dev.lajoscseppento.messyfiles.design.datastore.arangodb;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.model.CollectionsReadOptions;
import dev.lajoscseppento.messyfiles.design.datastore.arangodb.database.ArangoDbConnection;
import dev.lajoscseppento.messyfiles.design.datastore.core.MockFileSystemGenerator;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ArangoDbApp implements CommandLineRunner {

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
  }

  private void generateDatabase() throws Exception {
    log.info("Creating collection...");
    ArangoCollection collection = database.collection("file-system");
    collection.create();

    log.info("Populating collection...");

    FileSystem fileSystem = mockFileSystemGenerator.generate();

    for (Path rootDirectory : fileSystem.getRootDirectories()) {
      Files.walkFileTree(
          rootDirectory,
          new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
              BaseDocument document = new BaseDocument();

              document.addAttribute("path", file.toString());
              document.addAttribute("directory", file.getParent().toString());
              document.addAttribute("name", file.getFileName().toString());

              collection.insertDocument(document);

              return FileVisitResult.CONTINUE;
            }
          });
    }
  }

  private void cleanDatabase() {
    for (CollectionEntity collection : getNonSystemCollections()) {
      log.info("Deleting collection {}", collection.getName());
      database.collection(collection.getName()).drop();
    }
  }

  private Collection<CollectionEntity> getNonSystemCollections() {
    CollectionsReadOptions options = new CollectionsReadOptions();
    options.excludeSystem(true);
    return database.getCollections(options);
  }
}
