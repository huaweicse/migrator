package com.huaweicse.tools.migrator.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：
 *   在项目的 src/main/resources 目录下添加 bootstrap.yml 文件。
 */
public abstract class AddBootstrapYamlAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddBootstrapYamlAction.class);

  public static final String BASE_PATH = System.getProperty("user.dir");

  @Override
  public void run(String... args) {
    File folder = new File(args[0]);
    addYaml(folder);

    File[] files = folder.listFiles();
    if (files == null) {
      return;
    }
    Stream.of(files).forEach(this::addYaml);
  }

  private void addYaml(File file) {
    File child = new File(file, "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources");
    if (child.isDirectory()) {
      addYmlFile(new File(child, "bootstrap.yml"));
    }
  }

  private void addYmlFile(File file) {
    try {
      String originBootstrapContextPath = BASE_PATH + FILE_SEPARATOR + "templates";
      FileInputStream fileInputStream = new FileInputStream(
          originBootstrapContextPath + FILE_SEPARATOR + "bootstrap.yml");
      FileUtils.copyInputStreamToFile(fileInputStream, file);
    } catch (IOException ex) {
      // 如果添加配置文件失败，则后续进行手动添加
      LOGGER.error("add bootstrap.yml failed，need manual processing is required and message is {},", ex.getMessage());
    }
  }
}
