package dev.lajoscseppento.messyfiles.design.datastore.core;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
@Slf4j
public class MockFileSystemGeneratorDemo {
  public static void main(String[] args) {
    new SpringApplicationBuilder(MockFileSystemGeneratorDemo.class)
        .web(WebApplicationType.NONE)
        .run(args);
  }

  @Autowired private MockFileSystemGenerator generator;

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> demo();
  }

  @Bean
  public MockFileSystemGenerator mockFileSystemGenerator() {
    return new MockFileSystemGenerator(20201122150500L);
  }

  @Bean
  public MockFileSystemGeneratorProperties mockFileSystemGeneratorProperties() {
    return new MockFileSystemGeneratorProperties();
  }

  private void demo() throws Exception {
    log.info("Generating mock file system...");

    FileSystem fs = generator.generate(8);
    Path root = fs.getRootDirectories().iterator().next();

    long cnt = Files.walk(root).count();
    log.info("Generated elements: {}", cnt);
  }
}
