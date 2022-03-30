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
public class ModifyDubboMainClassActionTest {
  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyDubboMainClassAction modifyDubboMainClassAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
        + File.separator + Math.abs(new Random().nextInt());

    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyDubboMainClassActionTest"
            + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testProcessMainClass() throws Exception {
    modifyDubboMainClassAction.run(TEMP_DIR_PATH);
    String fileName = "DubboConsumerApplication.java";
    String outputFilePath =
        BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyDubboMainClassActionTest" + fileSeparator +
            "output" + fileSeparator + fileName;
    Utils.assertFileContentEquals(inputTestFilePath("dubbo-annotation", fileName), outputFilePath);
    Utils.assertFileContentEquals(inputTestFilePath("dubbo-springboot", fileName), outputFilePath);
    Utils.assertFileContentEquals(inputTestFilePath("dubbo-xml", fileName), outputFilePath);
  }

  private String inputTestFilePath(String styleType, String fileName) {
    return TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + styleType + fileSeparator + fileName;
  }
}
