package dev.lajoscseppento.messyfiles.design.datastore.core;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@RequiredArgsConstructor
@Slf4j
@SpringBootApplication
public class MockFileSystemGeneratorDemo implements CommandLineRunner {
  public static void main(String[] args) {
    new SpringApplicationBuilder(MockFileSystemGeneratorDemo.class)
        .web(WebApplicationType.NONE)
        .run(args);
  }

  private final MockFileSystemGenerator generator;

  @Override
  public void run(String... args) throws IOException {
    log.info("Generating mock file system...");

    FileSystem fs = generator.generate(8);
    Path root = fs.getRootDirectories().iterator().next();

    long cnt = Files.walk(root).count();
    log.info("Generated elements: {}", cnt);
  }
}
