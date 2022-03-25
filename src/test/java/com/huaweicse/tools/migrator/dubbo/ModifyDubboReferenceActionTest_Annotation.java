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
public class ModifyDubboReferenceActionTest_Annotation {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyDubboReferenceAction modifyDubboReferenceAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir") + fileSeparator + Math.abs(new Random().nextInt());
    FileUtils.copyDirectoryToDirectory(new File(BASE_PATH + fileSeparator
            + "testfiles" + fileSeparator + "ModifyDubboReferenceActionTest" + fileSeparator + "input" + fileSeparator
            + "dubbo-annotation"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }


  @Test
  public void testModifyDubboReferenceActionTest() throws Exception {
    modifyDubboReferenceAction.run(TEMP_DIR_PATH + fileSeparator + "dubbo-annotation");
    String consumerControllerFileName = "DubboConsumerController.java";
    String interfaceConfigFileName = "DubboInterfaceConfig.java";
    String baseCommonPath = BASE_PATH + fileSeparator + "testfiles" + fileSeparator +
        "ModifyDubboReferenceActionTest" + fileSeparator + "output" + fileSeparator;
    String tempCommonPath =
        TEMP_DIR_PATH + fileSeparator + "dubbo-annotation" + fileSeparator + "consumer"
            + fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "java" + fileSeparator +
            "com" + fileSeparator + "huaweicse" + fileSeparator;
    Utils.assertFileContentEquals(baseCommonPath + consumerControllerFileName,
        tempCommonPath + "test" + fileSeparator + consumerControllerFileName);
    Utils.assertFileContentEquals(baseCommonPath + interfaceConfigFileName,
        tempCommonPath + "config" + fileSeparator + interfaceConfigFileName);
  }
}
