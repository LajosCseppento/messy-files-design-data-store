package dev.lajoscseppento.messyfiles.design.datastore.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootTest(
    classes = {MockFileSystemGenerator.class, MockFileSystemGeneratorProperties.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(MockFileSystemGeneratorTest.TestConfiguration.class)
class MockFileSystemGeneratorTest {
  @Autowired private GenericApplicationContext context;

  @Test
  void testGenerateCanBeDeterministic() throws Exception {
    // Given
    int maxDepth = 3;

    MockFileSystemGenerator generator1 =
        (MockFileSystemGenerator) context.getBean("mockFileSystemGenerator1");
    MockFileSystemGenerator generator2 =
        (MockFileSystemGenerator) context.getBean("mockFileSystemGenerator2");
    assertThat(generator1).isNotSameAs(generator2);

    // When
    FileSystem fs1 = generator1.generate(maxDepth);
    FileSystem fs2 = generator2.generate(maxDepth);

    // Then
    Set<String> fs1Files = collectAbsolutePathStrings(fs1);
    Set<String> fs2Files = collectAbsolutePathStrings(fs2);

    assertThat(fs1Files).containsExactlyInAnyOrderElementsOf(fs2Files);
  }

  private static Set<String> collectAbsolutePathStrings(FileSystem fileSystem) throws IOException {
    return Files.walk(fileSystem.getRootDirectories().iterator().next())
        .map(path -> path.toAbsolutePath().toString())
        .collect(Collectors.toSet());
  }

  static class TestConfiguration {
    private static long SEED = 20201122151000L;

    @Bean("mockFileSystemGenerator1")
    public MockFileSystemGenerator mockFileSystemGenerator1() {
      return new MockFileSystemGenerator(SEED);
    }

    @Bean("mockFileSystemGenerator2")
    public MockFileSystemGenerator mockFileSystemGenerator2() {
      return new MockFileSystemGenerator(SEED);
    }
  }
}
