package com.huaweicse.tools.migrator.anta;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.huaweicse.tools.migrator.hsf.ModifyHSFAction;

@SpringBootTest
public class MigrateSupport {
  private String BASE_PATH;

  private String TEMP_DIR_PATH;

  @Autowired
  private ModifyHSFAction modifyHSFAction;

  @BeforeEach
  public void setUp() throws Exception {
//    TEMP_DIR_PATH = "D:\\anta\\temp";
//    BASE_PATH = "D:\\anta\\anta_mw_goods";
//    BASE_PATH = "D:\\anta\\anta_mw_gateway_app";
//    BASE_PATH = "D:\\anta\\anta_mw_support";
    BASE_PATH = "D:\\anta\\anta_mw_core";
//
//    FileUtils.copyDirectoryToDirectory(
//        new File(BASE_PATH),
//        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
//    FileUtils.deleteDirectory(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testModifyHSF() throws Exception {
    modifyHSFAction.run(BASE_PATH);
  }
}
