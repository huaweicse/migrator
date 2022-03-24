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
public class ModifyDubboInterface2RestActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyDubboInterface2RestAction modifyDubboInterface2RestAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir") + fileSeparator + Math.abs(new Random().nextInt());
    FileUtils.copyDirectoryToDirectory(new File(BASE_PATH + fileSeparator
            + "testfiles" + fileSeparator + "ModifyDubboInterface2RestActionTest" + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }


  @Test
  public void testModifyDubboInterface2RestActionTest() throws Exception {
    modifyDubboInterface2RestAction.run(TEMP_DIR_PATH);
    String fileName = "HelloService.java";
    Utils.assertFileContentEquals(
        BASE_PATH + fileSeparator
            + "testfiles" + fileSeparator + "ModifyDubboInterface2RestActionTest" + fileSeparator
            + "output" + fileSeparator + fileName,
        TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + fileName);
  }
}
