package dev.lajoscseppento.messyfiles.design.datastore.arangodb.database;

import com.arangodb.ArangoDB;
import com.arangodb.mapping.ArangoJack;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public final class ArangoDbAccessPointFactory {
  public ArangoDB create(
      @NonNull String host, int port, @NonNull String username, @NonNull String password) {
    log.debug("Creating ArangoDB access point to {}:{} as {} ...", host, port, username);

    return new ArangoDB.Builder()
        .host(host, port)
        .user(username)
        .password(password)
        .serializer(new ArangoJack())
        .build();
  }
}
