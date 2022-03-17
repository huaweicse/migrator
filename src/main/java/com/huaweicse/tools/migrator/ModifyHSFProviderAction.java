package com.huaweicse.tools.migrator;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFProvider 标签，如果存在，将其替换为 @RestController。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFProviderAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFProviderAction.class);

  @Value("${hsf.provider.packageName:com.alibaba.boot.hsf.annotation.HSFProvider}")
  private String hSFProviderPackageName;

  @Value("${spring.requestMapping.packageName:org.springframework.web.bind.annotation.RequestMapping}")
  private String requestMappingPackageName;

  @Value("${spring.restController.packageName:org.springframework.web.bind.annotation.RestController}")
  private String restControllerPackageName;

  private static final String LINE_SEPARATOR = "line.separator";

  private static final String INTERFACE_REGEX_PATTERN = "[a-zA-Z]+(.class)";

  private static final String HSF_PROVIDER = "@HSFProvider";

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    replaceContent(acceptedFiles);
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, HSF_PROVIDER);
  }

  private void replaceContent(List<File> acceptedFiles) {
    acceptedFiles.forEach(file -> {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (String line : lines) {
          if (line.contains(hSFProviderPackageName)) {
            line = line.replace(hSFProviderPackageName, requestMappingPackageName);
            tempStream.write(line);
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            tempStream.write("import " + restControllerPackageName + ";");
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            continue;
          }
          if (line.trim().startsWith(HSF_PROVIDER)) {
            Pattern pattern = Pattern.compile(INTERFACE_REGEX_PATTERN);
            Matcher matcher = pattern.matcher(line);
            String tempRouter = null;
            while (matcher.find()) {
              tempRouter = matcher.group().replace(".class", "");
            }
            tempStream.write("@RestController");
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            tempStream.write(
                "@RequestMapping(\"/" + tempRouter.substring(0, 1).toLowerCase() + tempRouter.substring(1) + "\")");
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            continue;
          }
          tempStream.write(line);
          tempStream.append(System.getProperty(LINE_SEPARATOR));
        }
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception e) {
        LOGGER.error("file content replacement failed and message is {}", e.getMessage());
      }
    });
  }
}