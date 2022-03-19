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
import com.huaweicse.tools.migrator.hsf.ModifyHSFPomAction;

@SpringBootTest
public class ModifyHSFPomActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyHSFPomAction modifyHSFPomAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir") + fileSeparator + Math.abs(new Random().nextInt());
    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles"
            + fileSeparator + "ModifyPomActionTest" + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.forceDeleteOnExit(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testModifyHSFPomAction() throws Exception {
    modifyHSFPomAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    String targetPomFilePath =
        BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyPomActionTest" + fileSeparator + "output"
            + fileSeparator + "pom.xml";
    String modifiedPomFilePath =
        TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + "pom.xml";
    Utils.assertFileContentEquals(targetPomFilePath, modifiedPomFilePath);
  }
}
