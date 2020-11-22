package dev.lajoscseppento.smartfiles.design.datastore.core;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
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

  @Bean
  public CommandLineRunner commandLineRunner() {
    return args -> demo();
  }

  private void demo() throws Exception {
    log.info("Generating mock file system...");

    MockFileSystemGenerator generator = new MockFileSystemGenerator(20201122150500L);
    FileSystem fs = generator.generate(8);
    Path root = fs.getRootDirectories().iterator().next();

    long cnt = Files.walk(root).count();
    log.info("Generated elements: {}", cnt);
  }
}
