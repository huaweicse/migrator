package com.huaweicse.tools.migrator;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFConsumer 标签，如果存在，将其替换为 @FeignClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
public class ModifyHSFConsumerAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFConsumerAction.class);

  private static final String PROJECT_CATALOGUE = System.getProperty("user.dir");

  private ArrayList<File> fileList = new ArrayList<>();

  private static final String HSF_CONSUMER = "HSFConsumer";

  private static final String FEIGN_CLIENT = "FeignClient";

  @Override
  public void run(String... args) {
    File fileCatalogue = new File(PROJECT_CATALOGUE);
    File[] files = fileCatalogue.listFiles();
    if (files == null) {
      return;
    }
    filesAdd(files);
    replaceContent();
  }

  private void filesAdd(File[] files) {
    Arrays.stream(files).forEach(file -> {
      if (file.isFile() && file.getName().endsWith(".java")) {
        fileList.add(file);
      }
      if (file.isDirectory()) {
        filesAdd(file.listFiles());
      }
    });
  }

  private void replaceContent() {
    fileList.forEach(file -> {
      try {
        File targetFile = new File(file.getAbsolutePath());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(targetFile));
        CharArrayWriter tempStream = new CharArrayWriter();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          line = line.replaceAll(HSF_CONSUMER, FEIGN_CLIENT);
          tempStream.write(line);
          tempStream.append(System.getProperty("line.separator"));
        }
        bufferedReader.close();
        FileWriter fileWriter = new FileWriter(targetFile);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception e) {
        LOGGER.error("file content replacement failed and message is {}", e.getMessage());
      }
    });
  }
}
