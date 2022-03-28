package com.huaweicse.tools.migrator.eureka;

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
public class ModifyEurekaMainClassActionTest {
  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyEurekaMainClassAction modifyEurekaMainClassAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir")
        + File.separator + Math.abs(new Random().nextInt());

    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyEurekaMainClassActionTest"
            + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testProcessMainClass() throws Exception {
    modifyEurekaMainClassAction.run(TEMP_DIR_PATH);
    String providerFileName = "EurekaProviderApplication.java";
    String consumerFileName = "EurekaConsumerApplication.java";
    String inputConmonFilePath = TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator;
    String outputCommonFilePath =
        BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyEurekaMainClassActionTest" + fileSeparator +
            "output" + fileSeparator;
    Utils.assertFileContentEquals(inputConmonFilePath + providerFileName, outputCommonFilePath + providerFileName);
    Utils.assertFileContentEquals(inputConmonFilePath + consumerFileName, outputCommonFilePath + consumerFileName);
  }

}
