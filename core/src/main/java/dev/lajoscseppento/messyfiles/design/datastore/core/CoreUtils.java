package dev.lajoscseppento.messyfiles.design.datastore.core;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CoreUtils {
  public String getMessage() {
    return "Hello, World!";
  }

  public void requireMinMax(String minName, int min, String maxName, int max) {
    if (min > max) {
      String msg =
          String.format("%s=%d must not be greater than %s=%d", minName, min, maxName, max);
      throw new IllegalArgumentException(msg);
    }
  }
}
