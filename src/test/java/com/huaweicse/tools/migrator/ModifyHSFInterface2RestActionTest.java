package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModifyHSFInterface2RestActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyHSFInterface2RestAction modifyHSFInterface2RestAction;

  @Test
  public void testInterface2Rest() throws IOException {
    String localBaseFilePath = BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "input";
    String tempBaseFilePath = TEMP_DIR_PATH + fileSeparator + "input";
    FileUtils.copyDirectoryToDirectory(new File(localBaseFilePath),
        new File(TEMP_DIR_PATH));
    modifyHSFInterface2RestAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    String fileName = "HSFInterfaceService.java";
    Assert.assertTrue(IOUtils.contentEquals(new FileInputStream(localBaseFilePath + fileSeparator + fileName),
        new FileInputStream(tempBaseFilePath + fileSeparator + fileName)));
  }
}
