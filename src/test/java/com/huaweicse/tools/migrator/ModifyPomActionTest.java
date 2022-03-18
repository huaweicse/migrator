package com.huaweicse.tools.migrator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModifyPomActionTest {

  private static final String BASE_PATH = System.getProperty("user.dir");

  private String TEMP_DIR_PATH;

  private String fileSeparator = File.separator;

  @Autowired
  private ModifyPomAction modifyHSFPomAction;

  @BeforeEach
  public void setUp() throws Exception {
    TEMP_DIR_PATH = System.getProperty("java.io.tmpdir") + fileSeparator + Math.abs(new Random().nextInt());
    FileUtils.copyDirectoryToDirectory(
        new File(BASE_PATH + fileSeparator + "testfiles"
            + fileSeparator + "ModifyPomActionTest" + fileSeparator + "input"),
        new File(TEMP_DIR_PATH));
  }

  @AfterEach
  public void tearDown() throws Exception {
    FileUtils.forceDeleteOnExit(new File(TEMP_DIR_PATH));
  }

  @Test
  public void testModifyPom() throws Exception {
    modifyHSFPomAction.run(TEMP_DIR_PATH + fileSeparator + "input");
    String targetPomFilePath =
        BASE_PATH + fileSeparator + "testfiles" + fileSeparator + "ModifyPomActionTest" + fileSeparator + "output"
            + fileSeparator + "pom.xml";
    String modifiedPomFilePath =
        TEMP_DIR_PATH + fileSeparator + "input" + fileSeparator + "pom.xml";
    Assert.assertEquals(genOrderedLines(targetPomFilePath), genOrderedLines(modifiedPomFilePath));
  }

  private List<String> genOrderedLines(String path) throws IOException {
    List<String> contentLines = FileUtils.readLines(new File(path), StandardCharsets.UTF_8);
    Collections.sort(contentLines);
    return contentLines;
  }
}