package com.huaweicse.tools.migrator.dubbo;

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
public class ModifyDubboAddBootstrapYamlActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyDubboAddBootstrapYamlAction modifyDubboAddBootstrapYamlAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
        + File.separator + Math.abs(new Random().nextInt());

    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyDubboAddBootstrapYamlActionTest"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testAddBootstrapFile() throws Exception {
    modifyDubboAddBootstrapYamlAction.run(TEMP_DIR_PATH + fileSeparator + "ModifyDubboAddBootstrapYamlActionTest");
    String originBootstrapContextPath = BASE_PATH + fileSeparator + "templates" + fileSeparator + "bootstrap.yml";
    String newBootstrapFilePath =
        TEMP_DIR_PATH + fileSeparator + "ModifyDubboAddBootstrapYamlActionTest" + fileSeparator +
            "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator + "bootstrap.yml";
    Utils.assertFileContentEquals(originBootstrapContextPath, newBootstrapFilePath);
  }
}
