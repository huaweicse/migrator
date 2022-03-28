package com.huaweicse.tools.migrator.nacos;

import java.io.File;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.Utils;

@SpringBootTest
public class ModifyNacosActionTest {
  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyNacosAction modifyNacosAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
        + File.separator + Math.abs(new Random().nextInt());

    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyNacosActionTest"
            + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.forceDeleteOnExit(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testaddBootstrapFile() throws Exception {
    modifyNacosAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    Utils.assertFileContentEquals(addBootstrapFilePath("provider"), outputFilePath("bootstrap.yml"));
    Utils.assertFileContentEquals(addBootstrapFilePath("consumer"), outputFilePath("bootstrap.yml"));
    Utils.assertFileContentEquals(modifiedPomFilePath(""), outputFilePath("parent.pom.xml"));
    Utils.assertFileContentEquals(modifiedPomFilePath("provider"), outputFilePath("provider.pom.xml"));
    Utils.assertFileContentEquals(modifiedPomFilePath("consumer"), outputFilePath("consumer.pom.xml"));
  }

  private String modifiedPomFilePath(String role) {
    return TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + role + fileSeparator + "pom.xml";
  }

  private String addBootstrapFilePath(String role) {
    return TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + role + fileSeparator + "src" + fileSeparator
        + "main" + fileSeparator + "resources" + fileSeparator + "bootstrap.yml";
  }

  private String outputFilePath(String fileName) {
    return BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyNacosActionTest" + fileSeparator +
        "output" + fileSeparator + fileName;
  }
}
