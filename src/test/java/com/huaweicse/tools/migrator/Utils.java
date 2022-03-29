package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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

  public static void assertFolderAllFileContentEquals(String inputFileDir, String outFileDir) throws Exception {
    ArrayList<File> list = new ArrayList<>();
    List<File> inputFiles = allFiles(list, inputFileDir);
    List<File> outputFiles = allFiles(list, outFileDir);
    for (int i = 0; i < inputFiles.size(); i++) {
      try (FileInputStream fis1 = new FileInputStream(inputFiles.get(i))) {
        try (FileInputStream fis2 = new FileInputStream(outputFiles.get(i))) {
          if (!IOUtils.contentEquals(fis1, fis2)) {
            throw new AssertionError(String.format("file %s and %s not equal", outFileDir, outFileDir));
          }
        }
      }
    }
  }

  private static List<File> allFiles(List<File> fileList, String fileDir) {
    File[] files = new File(fileDir).listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        allFiles(fileList, file.getAbsolutePath());
        continue;
      }
      fileList.add(file);
    }
    return fileList;
  }
}
