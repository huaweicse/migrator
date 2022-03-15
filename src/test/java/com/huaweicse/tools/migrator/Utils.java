package com.huaweicse.tools.migrator;

import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;

public class Utils {
  public static void assertFileContentEquals(String file1, String file2) throws Exception {
    try (FileInputStream fis1 = new FileInputStream(file1)) {
      try (FileInputStream fis2 = new FileInputStream(file2)) {
        if (!IOUtils.contentEquals(fis1, fis2)) {
          throw new AssertionError(String.format("file %s and %s not equal", file1, file2));
        }
      }
    }
  }
}
