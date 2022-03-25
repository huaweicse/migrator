package com.huaweicse.tools.migrator.dubbo;

import java.io.File;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.Utils;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class ModifyDubboReferenceActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyDubboReferenceAction modifyDubboReferenceAction;

  @BeforeAll
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir") + fileSeparator + Math.abs(new Random().nextInt());
    FileUtils.copyDirectoryToDirectory(new File(BASE_PATH + fileSeparator
            + "testfiles" + fileSeparator + "ModifyDubboReferenceActionTest" + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
    modifyDubboReferenceAction.run(TEMP_DIR_PATH);
  }

  @AfterAll
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testModifyDubboReferenceActionTest_Springboot() throws Exception {
    String consumerControllerFileName = "DubboSpringBootConsumerController.java";
    String interfaceConfigFileName = "DubboInterfaceConfig.java";
    Utils.assertFileContentEquals(outputTestFilePath("dubbo-springboot", consumerControllerFileName),
        modifiedTestFilePath("dubbo-springboot") + "test" + fileSeparator + consumerControllerFileName);
    Utils.assertFileContentEquals(outputTestFilePath("dubbo-springboot", interfaceConfigFileName),
        modifiedTestFilePath("dubbo-springboot") + "config" + fileSeparator + interfaceConfigFileName);
  }

  @Test
  public void testModifyDubboReferenceActionTest_Annotation() throws Exception {
    String consumerControllerFileName = "DubboAnnotationConsumerController.java";
    String interfaceConfigFileName = "DubboInterfaceConfig.java";
    Utils.assertFileContentEquals(outputTestFilePath("dubbo-annotation", consumerControllerFileName),
        modifiedTestFilePath("dubbo-annotation") + "test" + fileSeparator + consumerControllerFileName);
    Utils.assertFileContentEquals(outputTestFilePath("dubbo-annotation", interfaceConfigFileName),
        modifiedTestFilePath("dubbo-annotation") + "config" + fileSeparator + interfaceConfigFileName);
  }

  private String outputTestFilePath(String styleName, String fileName) {
    return BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyDubboReferenceActionTest" +
        fileSeparator + "output" + fileSeparator + styleName + fileSeparator + fileName;
  }

  private String modifiedTestFilePath(String styleName) {
    return TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + styleName + fileSeparator + "consumer"
        + fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "java" + fileSeparator +
        "com" + fileSeparator + "huaweicse" + fileSeparator;
  }
}
