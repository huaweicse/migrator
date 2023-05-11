package com.huaweicse.tools.migrator.hsf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class CheckTooManyBodyAction extends FileAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyMybatisXmlAction.class);

  private static final Pattern TOO_MANY_BODY = Pattern.compile("RequestBody.*RequestBody.*");

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    for (File file : acceptedFiles) {
      if (file.getAbsolutePath().contains("target")) {
        continue;
      }
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      int lineNumber = 0;
      for (; lineNumber < lines.size(); lineNumber++) {
        String line = lines.get(lineNumber);
        if (TOO_MANY_BODY.matcher(line).find()) {
          LOGGER.error("Find too many bodies {} {}", lineNumber, file.getAbsolutePath());
        }
      }
    }
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, "interface");
  }
}
