package dev.lajoscseppento.messyfiles.design.datastore.arangodb.database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.mapping.ArangoJack;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArangoDbConnection {

  @Autowired private ArangoDbProperties properties;

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
        new ArangoDB.Builder()
            .host(properties.getHost(), properties.getPort())
            .user(properties.getUsername())
            .password(properties.getPassword())
            .serializer(new ArangoJack())
            .build();

    log.info("Selecting database {} ...", properties.getDatabase());
    database = arangoDb.db(properties.getDatabase());
  }

  @PreDestroy
  void preDestroy() {
    log.info("Closing connection...");
    arangoDb.shutdown();
  }
}
