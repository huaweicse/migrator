package com.huaweicse.tools.migrator;

public interface Action {
  String ERROR_MESSAGE = "Manual processing is required. Cause is [{}]."
      + "File is [{}]. Line is [{}]";

  String LINE_SEPARATOR = System.lineSeparator();

  default String name() {
    return this.getClass().getSimpleName();
  }

  void run(String... args) throws Exception;
}
