package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

  private String localFileBasePath;

  @Autowired
  private ModifyHSFProviderAction modifyHSFProviderAction;

  @Test
  public void testModifyHSFConsumerActionStandardConfig() throws IOException {
    try {
      FileUtil.copyFolder("D:\\m\\migrator\\testfiles\\input","D:\\m\\migrator\\testfiles\\output");
      modifyHSFProviderAction.run();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
