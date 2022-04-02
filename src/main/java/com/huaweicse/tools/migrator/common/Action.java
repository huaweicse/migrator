package com.huaweicse.tools.migrator.common;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;

public interface Action {
  String ERROR_MESSAGE = "Manual processing is required. Cause is [{}]."
      + "File is [{}]. Line is [{}]";

  String FILE_SEPARATOR = File.separator;

  default void writeLine(CharArrayWriter tempStream, String line) throws IOException {
    tempStream.write(line);
    tempStream.append(System.lineSeparator());
  }

  default String name() {
    return this.getClass().getSimpleName();
  }

  void run(String... args) throws Exception;
}
