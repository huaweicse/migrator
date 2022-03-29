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
  public void testModifyNacosAction() throws Exception {
    modifyNacosAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    Utils.assertFolderAllFileContentEquals(TEMP_DIR_PATH + fileSeparator + "input",
        BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyNacosActionTest" + fileSeparator + "output");
  }
}
