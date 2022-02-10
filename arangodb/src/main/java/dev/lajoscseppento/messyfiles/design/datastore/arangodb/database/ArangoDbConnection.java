package dev.lajoscseppento.messyfiles.design.datastore.arangodb.database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.DbName;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArangoDbConnection {

  private final ArangoDbProperties properties;

  private ArangoDB arangoDb;
  @Getter private ArangoDatabase database;

  @PostConstruct
  void postConstruct() {
    log.info(
        "Connecting to {}:{} as {} ...",
        properties.getHost(),
        properties.getPort(),
        properties.getUsername());

    arangoDb =
        ArangoDbAccessPointFactory.create(
            properties.getHost(),
            properties.getPort(),
            properties.getUsername(),
            properties.getPassword());

    log.info("Selecting database {} ...", properties.getDatabase());
    database = arangoDb.db(DbName.of(properties.getDatabase()));
  }

  @PreDestroy
  void preDestroy() {
    log.info("Closing connection...");
    arangoDb.shutdown();
  }
}
