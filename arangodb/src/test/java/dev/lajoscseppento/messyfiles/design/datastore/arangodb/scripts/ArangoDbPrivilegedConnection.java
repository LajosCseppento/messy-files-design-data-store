package dev.lajoscseppento.messyfiles.design.datastore.arangodb.scripts;

import com.arangodb.ArangoDB;
import dev.lajoscseppento.messyfiles.design.datastore.arangodb.database.ArangoDbAccessPointFactory;
import dev.lajoscseppento.messyfiles.design.datastore.arangodb.database.ArangoDbProperties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@Import(ArangoDbProperties.class)
public class ArangoDbPrivilegedConnection {

  private final ArangoDbProperties properties;

  @Getter private ArangoDB arangoDb;

  @PostConstruct
  void postConstruct() {
    log.info("Connecting to ArangoDB as privileged user...");

    arangoDb =
        ArangoDbAccessPointFactory.create(
            properties.getHost(),
            properties.getPort(),
            ScriptConstants.PRIVILEGED_USERNAME,
            ScriptConstants.PRIVILEGED_PASSWORD);
  }

  @PreDestroy
  void preDestroy() {
    log.info("Closing connection...");
    arangoDb.shutdown();
  }
}
