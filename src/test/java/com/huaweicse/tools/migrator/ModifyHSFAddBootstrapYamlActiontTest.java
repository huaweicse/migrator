package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModifyHSFAddBootstrapYamlActiontTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction;

  @Test
  public void testAddBootstrapFile() throws IOException {
    FileUtils.copyDirectoryToDirectory(new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
    modifyHSFAddBootstrapYamlAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    String originBootstrapContextPath = BASE_PATH + fileSeparator + "templates" + fileSeparator + "bootstrap.txt";
    String newBootstrapFilePath =
        TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + "resources" + fileSeparator + "bootstrap.yml";
    Assert.assertTrue(IOUtils
        .contentEquals(new FileInputStream(originBootstrapContextPath), new FileInputStream(newBootstrapFilePath)));
  }

}
