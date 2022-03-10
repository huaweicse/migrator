package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
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

  private String fileSeparator = File.separator;

  private String fileBasePath;

  // 记录被修改的文件名称，方便测试用例通过后进行内容复原
  private List<String> modifyFileName = new ArrayList<>();

  @Autowired
  private ModifyHSFConsumerAction modifyHSFConsumerAction;

  // 初始化基础路径及运行内容修改逻辑
  @BeforeAll
  public void init() {
    fileBasePath = BASE_PATH + fileSeparator + "testfiles";
    modifyHSFConsumerAction.run(fileBasePath + fileSeparator + "input");
  }

  // 还原被修改的文件内容，方便下次测试
  @AfterAll
  public void end() throws IOException {
    modifyFileName.forEach(fileName -> {
      try {
        IOUtils.copy(new FileInputStream(genFilePath("originfiles", fileName)),
            new FileOutputStream(genFilePath("input", fileName)));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  // 规范开发风格文件测试
  @Test
  public void testModifyHSFConsumerActionStandardConfig() throws IOException {
    String fileName = "HSFConsumerStandardConfig.java";
    modifyFileName.add(fileName);
    Assert.assertTrue(
        IOUtils.contentEquals(new FileInputStream(genFilePath("input", fileName)),
            new FileInputStream(genFilePath("output", fileName))));
  }

  // 非规范开发风格文件测试
  @Test
  public void testModifyHSFConsumerActionNonstandardConfig() throws IOException {
    String fileName = "HSFConsumerNonstandardConfig.java";
    modifyFileName.add(fileName);
    Assert.assertTrue(
        IOUtils.contentEquals(new FileInputStream(genFilePath("input", fileName)),
            new FileInputStream(genFilePath("output", fileName))));
  }

  // 测试添加bootstrap.yml是否成功
  @Test
  public void testAddBootstrapFile() throws IOException {
    // 避免在windows或者linux系统中测试带来的差异性
    String originBootstrapContextPath =
        BASE_PATH + fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator
            + "bootstrap.txt";
    String newBootstrapFilePath = genFilePath("input", "resources" + fileSeparator + "bootstrap.yml");
    Assert.assertTrue(IOUtils
        .contentEquals(new FileInputStream(originBootstrapContextPath), new FileInputStream(newBootstrapFilePath)));
    // 每次生成bootstrap.yml都会覆盖原来生成的内容，所以测试通过后没必要删除新增的bootstrap.yml文件
  }

  private String genFilePath(String type, String fileName) {
    return fileBasePath + fileSeparator + type + fileSeparator + fileName;
  }
}
