package dev.lajoscseppento.messyfiles.design.datastore.core;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Mock file system generator. Rudimentary, uses jimfs, Unix-style. */
@Component
@Slf4j
public class MockFileSystemGenerator {
  private final Random random;

  @Autowired private MockFileSystemGeneratorProperties properties;

  public MockFileSystemGenerator() {
    this.random = new Random();
  }

  public MockFileSystemGenerator(long seed) {
    this.random = new Random(seed);
  }

  public FileSystem generate() {
    return generate(properties.getDefaultMaxDepth());
  }

  public FileSystem generate(int maxDepth) {
    if (maxDepth < 1) {
      throw new IllegalArgumentException("Invalid maxDepth: " + maxDepth);
    }

    try {
      log.info("Generating mock file system with maxDepth={} ...", maxDepth);

      Configuration configuration =
          Configuration.unix().toBuilder().setWorkingDirectory("/").build();
      FileSystem fs = Jimfs.newFileSystem(configuration);

      Path root = fs.getRootDirectories().iterator().next();

      explode(root, 1, maxDepth);

      log.info("Generated mock file system with maxDepth={} ...", maxDepth);

      return fs;
    } catch (Exception ex) {
      throw new MockFileSystemGeneratorException("File system generation failed", ex);
    }
  }

  private void explode(Path directory, int depth, int maxDepth) throws IOException {
    if (depth > maxDepth) {
      return;
    }

    for (int i = 1;
        i <= nextInt(properties.getMinSubdirectories(), properties.getMaxSubdirectories());
        i++) {
      Path subdirectory = directory.resolve(String.format("d%02d", i));
      Files.createDirectories(subdirectory);

      explode(subdirectory, depth + 1, maxDepth);
    }

    for (int i = 1; i <= nextInt(properties.getMinFiles(), properties.getMaxFiles()); i++) {
      Path file = directory.resolve(String.format("f%02d.txt", i));
      Files.createFile(file);
    }
  }

  private int nextInt(int min, int max) {
    CoreUtils.requireMinMax("min", min, "max", max);
    return min + random.nextInt(max - min + 1);
  }
}
