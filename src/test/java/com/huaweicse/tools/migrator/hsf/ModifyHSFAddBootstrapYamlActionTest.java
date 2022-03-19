package com.huaweicse.tools.migrator.hsf;

import java.io.File;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.Utils;
import com.huaweicse.tools.migrator.hsf.ModifyHSFAddBootstrapYamlAction;

@SpringBootTest
public class ModifyHSFAddBootstrapYamlActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
        + File.separator + Math.abs(new Random().nextInt());

    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyHSFAddBootstrapYamlActionTest"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testAddBootstrapFile() throws Exception {
    modifyHSFAddBootstrapYamlAction.run(TEMP_DIR_PATH + fileSeparator + "ModifyHSFAddBootstrapYamlActionTest");
    String originBootstrapContextPath = BASE_PATH + fileSeparator + "templates" + fileSeparator + "bootstrap.yml";
    String newBootstrapFilePath =
        TEMP_DIR_PATH + fileSeparator + "ModifyHSFAddBootstrapYamlActionTest" + fileSeparator +
            "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator + "bootstrap.yml";
    Utils.assertFileContentEquals(originBootstrapContextPath, newBootstrapFilePath);
  }
}
