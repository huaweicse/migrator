package com.huaweicse.tools.migrator;

import java.io.File;

public interface Action {

  default String name() {
    return this.getClass().getSimpleName();
  }

  default File[] allFiles(String... args) {
    File fileCatalogue = new File(args[0]);
    return fileCatalogue.listFiles();
  }

  void run(String... args);
}
