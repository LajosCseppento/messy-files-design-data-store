package dev.lajoscseppento.messyfiles.design.datastore.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("messy-files.design.MockFileSystemGenerator")
@Data
public class MockFileSystemGeneratorProperties {
  private int defaultMaxDepth = 6;
  private int minSubdirectories = 4;
  private int maxSubdirectories = 10;
  private int minFiles = 0;
  private int maxFiles = 8;

  public void validate() {
    CoreUtils.requireMinMax(
        "minSubdirectories", minSubdirectories, "maxSubdirectories", maxSubdirectories);
    CoreUtils.requireMinMax("minFiles", minFiles, "maxFiles", maxFiles);
  }
}
