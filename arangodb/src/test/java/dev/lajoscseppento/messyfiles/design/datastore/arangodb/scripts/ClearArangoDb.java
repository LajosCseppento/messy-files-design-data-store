package dev.lajoscseppento.messyfiles.design.datastore.arangodb.scripts;

import com.arangodb.ArangoDB;
import com.arangodb.DbName;
import com.arangodb.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

/** Clears all users and databases from the ArangoDB instance. */
@RequiredArgsConstructor
@Slf4j
@EnableAutoConfiguration
@Import(ArangoDbPrivilegedConnection.class)
public class ClearArangoDb implements CommandLineRunner {

  private final ArangoDbPrivilegedConnection privilegedConnection;

  public static void main(String[] args) {
    new SpringApplicationBuilder(ClearArangoDb.class)
        .web(WebApplicationType.NONE)
        .run(args)
        .close();
  }

  public void run(String... args) {
    ArangoDB arangoDb = privilegedConnection.getArangoDb();

    for (String database : arangoDb.getDatabases()) {

      if (DbName.SYSTEM.get().equals(database)) {
        log.info("Keeping database {}", database);
      } else {
        log.info("Deleting database {} ...", database);
        arangoDb.db(DbName.of(database)).drop();
      }
    }

    for (UserEntity user : arangoDb.getUsers()) {
      String username = user.getUser();

      if (ScriptConstants.PRIVILEGED_USERNAME.equals(username)) {
        log.info("Keeping user {}", username);
      } else {
        log.info("Deleting user {} ...", username);
        arangoDb.deleteUser(username);
      }
    }

    log.info("Done.");
  }
}
