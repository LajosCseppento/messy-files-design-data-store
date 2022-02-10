package dev.lajoscseppento.messyfiles.design.datastore.arangodb.scripts;

import com.arangodb.ArangoDB;
import com.arangodb.DbName;
import com.arangodb.entity.Permissions;
import com.arangodb.entity.UserEntity;
import dev.lajoscseppento.messyfiles.design.datastore.arangodb.database.ArangoDbProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

/** Initialises ArangoDB user and database. */
@RequiredArgsConstructor
@Slf4j
@EnableAutoConfiguration
@Import(ArangoDbPrivilegedConnection.class)
public class InitialiseArangoDb implements CommandLineRunner {

  private final ArangoDbPrivilegedConnection privilegedConnection;
  private final ArangoDbProperties properties;

  public static void main(String[] args) {
    new SpringApplicationBuilder(InitialiseArangoDb.class)
        .web(WebApplicationType.NONE)
        .run(args)
        .close();
  }

  public void run(String... args) {
    ArangoDB arangoDb = privilegedConnection.getArangoDb();

    log.info("Creating user {} ...", properties.getUsername());
    UserEntity user = arangoDb.createUser(properties.getUsername(), properties.getPassword());

    log.info("Creating database {} ...", properties.getDatabase());
    DbName dbName = DbName.of(properties.getDatabase());
    arangoDb.createDatabase(dbName);

    log.info("Granting permissions...");
    arangoDb.db(dbName).grantAccess(user.getUser(), Permissions.RW);

    log.info("Done.");
  }
}
