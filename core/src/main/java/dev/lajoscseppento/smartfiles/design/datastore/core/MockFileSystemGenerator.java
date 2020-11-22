package dev.lajoscseppento.smartfiles.design.datastore.core;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

/** Mock file system generator. Rudimentary, uses jimfs, Unix-style. */
@Slf4j
public class MockFileSystemGenerator {

  // These parameters generate max 2M elements under 10 seconds
  private static final int MIN_SUBDIRECTORIES = 4;
  private static final int MAX_SUBDIRECTORIES = 10;
  private static final int MIN_FILES = 0;
  private static final int MAX_FILES = 8;
  private static final int DEFAULT_MAX_DEPTH = 6;

  private final Random random;

  public MockFileSystemGenerator() {
    this.random = new Random();
  }

  public MockFileSystemGenerator(long seed) {
    this.random = new Random(seed);
  }

  public FileSystem generate() {
    return generate(DEFAULT_MAX_DEPTH);
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

    for (int i = 1; i <= nextInt(MIN_SUBDIRECTORIES, MAX_SUBDIRECTORIES); i++) {
      Path subdirectory = directory.resolve(String.format("d%02d", i));
      Files.createDirectories(subdirectory);

      explode(subdirectory, depth + 1, maxDepth);
    }

    for (int i = 1; i <= nextInt(MIN_FILES, MAX_FILES); i++) {
      Path file = directory.resolve(String.format("f%02d.txt", i));
      Files.createFile(file);
    }
  }

  private int nextInt(int min, int max) {
    if (min > max) {
      String msg = String.format("min=%d must not be greater than max=%d", min, max);
      throw new IllegalArgumentException(msg);
    }
    return min + random.nextInt(max - min + 1);
  }
}
