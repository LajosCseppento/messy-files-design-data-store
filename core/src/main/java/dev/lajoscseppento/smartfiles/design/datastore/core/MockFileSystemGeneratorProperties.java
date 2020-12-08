package dev.lajoscseppento.smartfiles.design.datastore.core;

import java.util.OptionalInt;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("smartfiles.design.MockFileSystemGenerator")
@Data
public class MockFileSystemGeneratorProperties {
  private int depth = 6;
  private int minSubdirectories = 4;
  private int maxSubdirectories = 10;
  private int minFiles = 0;
  private int maxFiles = 8;
  @NonNull private OptionalInt seed = OptionalInt.empty();

  public void validate() {
    // TODO
    //    Verify.
  }
}
