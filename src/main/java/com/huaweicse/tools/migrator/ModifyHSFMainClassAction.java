package com.huaweicse.tools.migrator;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含main函数，并将相关 pandora 逻辑改为 Spring Boot。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFMainClassAction extends FileAction {
  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    replaceContent(acceptedFiles);
  }

  private void replaceContent(List<File> acceptedFiles) throws Exception {
    for (File file : acceptedFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      CharArrayWriter tempStream = new CharArrayWriter();

      boolean importBegin = false;
      boolean classAnnotationBegin = false;
      boolean mainBegin = false;

      for (String line : lines) {
        // import section
        if (line.startsWith("import")) {
          if (!importBegin) {
            tempStream.append("import org.springframework.cloud.client.discovery.EnableDiscoveryClient;");
            tempStream.append(LINE_SEPARATOR);
            tempStream.append("import org.springframework.cloud.openfeign.EnableFeignClients;");
            tempStream.append(LINE_SEPARATOR);
            importBegin = true;
          }

          if (line.contains("com.taobao.pandora.boot.PandoraBootstrap") ||
              line.contains("org.springframework.cloud.client.discovery.EnableDiscoveryClient") ||
              line.contains("org.springframework.cloud.openfeign.EnableFeignClients")) {
            continue;
          }

          tempStream.append(line);
          tempStream.append(LINE_SEPARATOR);
          continue;
        }

        // class annotation section
        if (line.startsWith("@")) {
          if (!classAnnotationBegin) {
            tempStream.append("@EnableDiscoveryClient");
            tempStream.append(LINE_SEPARATOR);
            tempStream.append("@EnableFeignClients");
            tempStream.append(LINE_SEPARATOR);
            classAnnotationBegin = true;
          }

          if (line.contains("EnableDiscoveryClient") || line.contains("EnableFeignClients")) {
            continue;
          }

          tempStream.append(line);
          tempStream.append(LINE_SEPARATOR);
          continue;
        }

        // main section
        if (line.contains("static void main")) {
          mainBegin = true;
        }

        if (mainBegin) {
          if (line.contains("PandoraBootstrap")) {
            continue;
          }
        }

        // other
        tempStream.append(line);
        tempStream.append(LINE_SEPARATOR);
      }

      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, "static void main");
  }
}
