package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ModifyHSFAddBootstrapYamlAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFAddBootstrapYamlAction.class);

  public static final String FILE_SEPARATOR = File.separator;

  public static final String BASE_PATH = System.getProperty("user.dir");

  @Override
  public void run(String... args) {
    File[] files = allFiles(args[0]);
    if (files == null){
      return;
    }
    addYaml(files);
  }

  private void addYaml(File[] files) {
    Arrays.stream(files).forEach(file -> {
      if (file.isDirectory() && "resources".equals(file.getName())) {
        addYmlFile(new File(file.getAbsolutePath() + FILE_SEPARATOR + "bootstrap.yml"));
      }
    });
  }

  private void addYmlFile(File file) {
    try {
      // 避免在windows或者linux系统中带来的差异性
      String originBootstrapContextPath =
          BASE_PATH + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources";
      FileInputStream fileInputStream = new FileInputStream(
          originBootstrapContextPath + FILE_SEPARATOR + "bootstrap.txt");
      FileUtils.copyInputStreamToFile(fileInputStream, file);
    } catch (IOException ex) {
      // 如果添加配置文件失败，则后续进行手动添加
      LOGGER.error("add bootstrap.yml failed，need manual processing is required and message is {},", ex.getMessage());
    }
  }
}
