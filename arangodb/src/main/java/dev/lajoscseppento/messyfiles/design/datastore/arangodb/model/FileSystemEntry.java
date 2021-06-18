package dev.lajoscseppento.messyfiles.design.datastore.arangodb.model;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;
import lombok.Data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

@Data
public class FileSystemEntry {
  @DocumentField(Type.ID)
  private String id;

  @DocumentField(Type.KEY)
  private String key;

  @DocumentField(Type.REV)
  private String revision;

  private String path;
  private FileSystemEntryType type;
  private String parent;
  private String name;
  private Long size;

  public static FileSystemEntry create(Path path, BasicFileAttributes attributes) {
    path = path.toAbsolutePath().normalize();
    FileSystemEntry entry = new FileSystemEntry();
    entry.setPath(path.toString());

    if (Files.isDirectory(path)) {
      if (path.getParent() == null) {
        entry.setType(FileSystemEntryType.FILE_SYSTEM_ROOT);
      } else {
        entry.setType(FileSystemEntryType.DIRECTORY);
        entry.setParent(path.getParent().toString());
        entry.setName(path.getFileName().toString());
      }
    } else if (Files.isRegularFile(path)) {
      entry.setType(FileSystemEntryType.REGULAR_FILE);
      entry.setParent(path.getParent().toString());
      entry.setName(path.getFileName().toString());
      entry.setSize(attributes.size());
    } else {
      throw new AssertionError("Not supported: " + path);
    }

    return entry;
  }
}
