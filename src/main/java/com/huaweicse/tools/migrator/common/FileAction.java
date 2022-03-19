package com.huaweicse.tools.migrator.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public abstract class FileAction implements Action {
  protected List<File> acceptedFiles(String folder) throws IOException {
    List<File> result = new ArrayList<>();
    File file = new File(folder);
    addFiles(file, result);
    return result;
  }

  private void addFiles(File file, List<File> result) throws IOException {
    if (file.isDirectory()) {
      File[] children = file.listFiles();
      assert children != null;
      for (File child : children) {
        addFiles(child, result);
      }
      return;
    }

    if (isAcceptedFile(file)) {
      result.add(file);
    }
  }

  protected abstract boolean isAcceptedFile(File file) throws IOException;

  protected boolean fileContains(File file, String pattern) throws IOException {
    String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    return content.contains(pattern);
  }
}
