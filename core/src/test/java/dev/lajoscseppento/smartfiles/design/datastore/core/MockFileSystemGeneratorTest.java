package dev.lajoscseppento.smartfiles.design.datastore.core;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MockFileSystemGeneratorTest {
  @Test
  void testGenerateCanBeDeterministic() throws Exception {
    // Given
    long seed = 20201122151000L;
    int maxDepth = 3;

    MockFileSystemGenerator generator1 = new MockFileSystemGenerator(seed);
    MockFileSystemGenerator generator2 = new MockFileSystemGenerator(seed);

    // When
    FileSystem fs1 = generator1.generate(maxDepth);
    FileSystem fs2 = generator2.generate(maxDepth);

    // Then
    Set<String> fs1Files = collectAbsolutePathStrings(fs1);
    Set<String> fs2Files = collectAbsolutePathStrings(fs2);

    Assertions.assertThat(fs1Files).containsExactlyInAnyOrderElementsOf(fs2Files);
  }

  private static Set<String> collectAbsolutePathStrings(FileSystem fileSystem) throws IOException {
    return Files.walk(fileSystem.getRootDirectories().iterator().next())
        .map(path -> path.toAbsolutePath().toString())
        .collect(Collectors.toSet());
  }
}
