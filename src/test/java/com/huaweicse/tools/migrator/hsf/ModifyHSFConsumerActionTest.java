package com.huaweicse.tools.migrator.hsf;

import java.io.File;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.Utils;
import com.huaweicse.tools.migrator.hsf.ModifyHSFConsumerAction;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class ModifyHSFConsumerActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  private String localFileBasePath;

  @Autowired
  private ModifyHSFConsumerAction modifyHSFConsumerAction;


  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
        + File.separator + Math.abs(new Random().nextInt());

    FileUtils.copyDirectoryToDirectory(new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception  {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  // 规范开发风格文件测试
  @Test
  public void testModifyHSFConsumerActionStandardConfig() throws Exception {
    localFileBasePath = BASE_PATH + fileSeparator + "testfiles";
    FileUtils.copyDirectoryToDirectory(new File(localFileBasePath + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
    modifyHSFConsumerAction.run(TEMP_DIR_PATH + fileSeparator + "input");

    String fileName = "HSFConsumerStandardConfig.java";
    Utils.assertFileContentEquals(genFilePath(TEMP_DIR_PATH, "input", fileName),
        genFilePath(localFileBasePath, "output", fileName));
  }

  // 非规范开发风格文件测试
  @Test
  public void testModifyHSFConsumerActionNonstandardConfig() throws Exception {
    localFileBasePath = BASE_PATH + fileSeparator + "testfiles";
    FileUtils.copyDirectoryToDirectory(new File(localFileBasePath + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
    modifyHSFConsumerAction.run(TEMP_DIR_PATH + fileSeparator + "input");

    String fileName = "HSFConsumerNonstandardConfig.java";
    Utils.assertFileContentEquals(genFilePath(TEMP_DIR_PATH, "input", fileName),
        genFilePath(localFileBasePath, "output", fileName));
  }

  private String genFilePath(String fileBasePath, String type, String fileName) {
    return fileBasePath + fileSeparator + type + fileSeparator + fileName;
  }
}
