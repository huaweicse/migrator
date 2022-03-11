package com.huaweicse.tools.migrator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述：
 * 扫描目录下面的所有JAVA文件，首先扫描出包含 @HSFProvider 标签的文件，并从中解析出需要处理的 JAVA interface文件，
 * 然后将 interface 文件修改为 REST 风格。 替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFInterface2RestAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFInterface2RestAction.class);

  private static final String INTERFACE_REGEX_PATTERN = "[a-zA-Z]+(.class)";

  private static final String ROUTER_REGEX_PATTERN = "[/*{}]";

  private static final String LINE_SEPARATOR = "line.separator";

  // 保存扫描到的所有java文件
  private ArrayList<File> fileList = new ArrayList<>();

  // 保存扫描到的需要改动的接口java文件名称
  private ArrayList<String> interfaceFileList = new ArrayList<>();

  private static final String HSF_PROVIDER = "@HSFProvider";

  @Value("${spring.responseBody.packageName:org.springframework.web.bind.annotation.ResponseBody}")
  private String responseBodyPackageName;

  @Value("${spring.postMapping.packageName:org.springframework.web.bind.annotation.PostMapping}")
  private String postMappingPackageName;

  @Override
  public void run(String... args) {
    File[] files = allFiles(args[0]);
    if (files == null) {
      return;
    }
    filesAdd(files);
    filterInterfaceFile();
    replaceContent();
  }

  private void filesAdd(File[] files) {
    Arrays.stream(files).forEach(file -> {
      if (file.isFile() && file.getName().endsWith(".java")) {
        fileList.add(file);
      }
      if (file.isDirectory()) {
        filesAdd(file.listFiles());
      }
    });
  }

  private void filterInterfaceFile() {
    for (File file : fileList) {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        for (String line : lines) {
          if (line.contains(HSF_PROVIDER)) {
            Pattern pattern = Pattern.compile(INTERFACE_REGEX_PATTERN);
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
              interfaceFileList.add(matcher.group().replace(".class", ".java"));
            }
            break;
          }
        }
      } catch (IOException e) {
        LOGGER.error("error filtering interface file and filePath is {}", file.getAbsolutePath());
      }
    }
  }

  private void replaceContent() {
    fileList.forEach(file -> {
      try {
        CharArrayWriter tempStream = new CharArrayWriter();
        String tempInterfaceFileName = null;
        for (String interfaceFileName : interfaceFileList) {
          if (interfaceFileName.equals(file.getName())) {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
              if (line.contains("package")) {
                writeLine(tempStream, line);
                writeLine(tempStream, "");
                writeLine(tempStream, "import " + responseBodyPackageName + ";");
                writeLine(tempStream, "import " + postMappingPackageName + ";");
                continue;
              }
              if (!("".equals(line) || isEffectiveInterface(line))) {
                String router = line.trim().replace("(", " ").split(" ")[1];
                writeLine(tempStream, "  @ResponseBody");
                writeLine(tempStream, "  @PostMapping(value = \"/" + router + "\")");
                writeLine(tempStream, line);
                continue;
              }
              writeLine(tempStream, line);
            }
            tempInterfaceFileName = interfaceFileName;
            FileWriter fileWriter = new FileWriter(file);
            tempStream.writeTo(fileWriter);
            fileWriter.close();
          }
        }
        interfaceFileList.remove(tempInterfaceFileName);
      } catch (IOException e) {
        LOGGER.error("error modifying content and filePath is {}", file.getAbsolutePath());
      }
    });
  }

  private boolean isEffectiveInterface(String line) {
    Pattern pattern = Pattern.compile(ROUTER_REGEX_PATTERN);
    Matcher matcher = pattern.matcher(line);
    return matcher.find();
  }

  private void writeLine(CharArrayWriter tempStream, String line) throws IOException {
    tempStream.write(line);
    tempStream.append(System.getProperty(LINE_SEPARATOR));
  }
}
