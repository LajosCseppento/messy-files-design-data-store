package dev.lajoscseppento.messyfiles.design.datastore.arangodb.database;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("messy-files.design.arango-db")
@Data
public class ArangoDbProperties {
  private String host;
  private int port;
  private String username;
  private String password;
  private String database;
}
