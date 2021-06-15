package dev.lajoscseppento.messyfiles.design.datastore.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("messy-files.design.mock-file-system-generator")
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
