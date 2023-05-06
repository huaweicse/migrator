package com.huaweicse.tools.migrator.hsf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class CopyDiffFileAction extends FileAction {
  @Override
  public void run(String... args) throws Exception {
    String newFolder = args[0];
    String oldFolder = args[1];
    String diffFolder = args[2];

    List<File> allFiles = acceptedFiles(newFolder);
    for (File file : allFiles) {
      long newCheckSum = FileUtils.checksumCRC32(file);
      File oldFile = new File(file.getAbsolutePath().replace(newFolder, oldFolder));
      if (oldFile.exists()) {
        long oldCheckSum = FileUtils.checksumCRC32(oldFile);
        if (newCheckSum == oldCheckSum) {
          continue;
        }
      }

      File diffFile = new File(file.getAbsolutePath().replace(newFolder, diffFolder));
      FileUtils.copyFile(file, diffFile);
    }
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    String name = file.getAbsolutePath();
    if (name.contains(File.separator + "target" + File.separator)) {
      return false;
    }
    if (name.contains(File.separator + ".git" + File.separator)) {
      return false;
    }
    if (name.contains(File.separator + ".idea" + File.separator)) {
      return false;
    }
    return true;
  }
}
