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
public class ModifyHSFProviderActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyHSFProviderAction modifyHSFProviderAction;

  @Test
  public void testModifyHSFProviderAction() throws IOException {
    String localBaseFilePath = BASE_PATH + fileSeparator + "testfiles";
    String tempBaseFilePath = TEMP_DIR_PATH + "input";
    FileUtils.copyDirectoryToDirectory(new File(localBaseFilePath + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
    modifyHSFProviderAction.run(tempBaseFilePath);
    String fileName = "HSFInterfaceServiceImpl.java";
    Assert.assertTrue(IOUtils.contentEquals(
        new FileInputStream(localBaseFilePath + fileSeparator + "output" + fileSeparator + fileName),
        new FileInputStream(tempBaseFilePath + fileSeparator + fileName)));
  }

}