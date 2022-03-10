package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class MigratorApplicationTests {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private static final String TEMP_DIR_PATH = System.getProperty("java.io.tmpdir");

  private String fileSeparator = File.separator;

  private String localFileBasePath;

  @Autowired
  private ModifyHSFConsumerAction modifyHSFConsumerAction;

  @Autowired
  private ModifyHSFAddBootstrapYamlAction modifyHSFAddBootstrapYamlAction;

  // 初始化基础路径及运行内容修改逻辑
  @BeforeAll
  public void init() throws IOException {
    localFileBasePath = BASE_PATH + fileSeparator + "testfiles";
    FileUtils.copyDirectoryToDirectory(new File(localFileBasePath + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
    modifyHSFConsumerAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    modifyHSFAddBootstrapYamlAction.run(TEMP_DIR_PATH + fileSeparator + "input");
  }

  // 规范开发风格文件测试
  @Test
  public void testModifyHSFConsumerActionStandardConfig() throws IOException {
    String fileName = "HSFConsumerStandardConfig.java";
    Assert.assertTrue(
        IOUtils.contentEquals(new FileInputStream(genFilePath(TEMP_DIR_PATH, "input", fileName)),
            new FileInputStream(genFilePath(localFileBasePath, "output", fileName))));
  }

  // 非规范开发风格文件测试
  @Test
  public void testModifyHSFConsumerActionNonstandardConfig() throws IOException {
    String fileName = "HSFConsumerNonstandardConfig.java";
    Assert.assertTrue(
        IOUtils.contentEquals(new FileInputStream(genFilePath(TEMP_DIR_PATH, "input", fileName)),
            new FileInputStream(genFilePath(localFileBasePath, "output", fileName))));
  }

  // 测试添加bootstrap.yml是否成功
  @Test
  public void testAddBootstrapFile() throws IOException {
    // 避免在windows或者linux系统中测试带来的差异性
    String originBootstrapContextPath =
        BASE_PATH + fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator
            + "bootstrap.txt";
    String newBootstrapFilePath =
        genFilePath(TEMP_DIR_PATH, "input", "resources" + fileSeparator + "bootstrap.yml");
    Assert.assertTrue(IOUtils
        .contentEquals(new FileInputStream(originBootstrapContextPath), new FileInputStream(newBootstrapFilePath)));
    // 每次生成bootstrap.yml都会覆盖原来生成的内容，所以测试通过后没必要删除新增的bootstrap.yml文件
  }

  private String genFilePath(String fileBasePath, String type, String fileName) {
    return fileBasePath + fileSeparator + type + fileSeparator + fileName;
  }
}
