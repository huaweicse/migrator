package com.huaweicse.tools.migrator;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MigratorApplicationTests {

  private final String BASE_PATH = System.getProperty("user.dir");

  private final String HSF_CONSUMER_PACKAGE_NAME = "com.alibaba.boot.hsf.annotation.HSFConsumer";

  private final String FEIGN_CLIENT_PACKAGE_NAME = "org.springframework.cloud.openfeign.FeignClient";

  @Autowired
  private ModifyHSFConsumerAction modifyHSFConsumerAction;

  @Test
  void testModifyHSFConsumerAction() throws IOException {
    String dirPath = BASE_PATH + "\\src\\test\\testfiles";
    modifyHSFConsumerAction.run(dirPath + "\\input", HSF_CONSUMER_PACKAGE_NAME, FEIGN_CLIENT_PACKAGE_NAME);
    Assert.assertTrue(IOUtils.contentEquals(new FileInputStream(dirPath + "\\input\\HSFConsumer.java"),
        new FileInputStream(dirPath + "\\output\\HSFConsumer.java")));
  }
}
