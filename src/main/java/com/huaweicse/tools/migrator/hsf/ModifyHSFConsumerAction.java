package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Const;
import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFConsumer 标签，如果存在，将其替换为 @FeignClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFConsumerAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFConsumerAction.class);

  private static final String HSF_CONSUMER = "@HSFConsumer";

  private static final Pattern HSF_SERVICE_GROUP = Pattern.compile("serviceGroup = \"[a-zA-Z-_]+\"");

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, HSF_CONSUMER);
  }

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    replaceContent(acceptedFiles);
  }

  private void replaceContent(List<File> acceptedFiles) {
    acceptedFiles.forEach(file -> {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        String className = "";
        boolean notesBegin = false;

        for (int i = 0; i < lines.size(); i++) {
          String line = lines.get(i);

          // 空行
          if (line.trim().isEmpty()) {
            writeLine(tempStream, line);
            continue;
          }
          // 行注释
          if (line.trim().startsWith("//")) {
            writeLine(tempStream, line);
            continue;
          }
          // 文本注释
          if (line.trim().startsWith("*/")) {
            notesBegin = false;
            writeLine(tempStream, line);
            continue;
          }
          if (notesBegin) {
            writeLine(tempStream, line);
            continue;
          }
          if (line.trim().startsWith("/**")) {
            notesBegin = true;
            writeLine(tempStream, line);
            continue;
          }

          if (line.contains("import")) {
            line = line.replace(Const.HSF_CONSUMER_PACKAGE_NAME, Const.FEIGN_CLIENT_PACKAGE_NAME);
            writeLine(tempStream, line);
            continue;
          }

          if (line.contains(" class ")) {
            className = line.substring(line.indexOf(" class ") + 7, line.indexOf("{")).trim();
            writeLine(tempStream, line);
            continue;
          }

          // 处理@HSFConsumer注解信息及接口信息
          if (line.contains(HSF_CONSUMER)) {
            String nextLine = lines.get(i + 1);
            String[] interfaceLine = nextLine.trim().split(" ");
            if (interfaceLine.length < 3) {
              LOGGER.error(ERROR_MESSAGE,
                  "Interface definition not valid under @HSFConsumer annotation.",
                  file.getAbsolutePath(), i);
              writeLine(tempStream, line);
            } else {
              String interfaceName = interfaceLine[1];
              String feignClientInfo = feignClientInfo(line, interfaceName, className);
              if (feignClientInfo != null) {
                line = line.replace(line.trim(), feignClientInfo);
                nextLine = nextLine.replace(nextLine.trim(), interfaceExtension(nextLine));
                writeLine(tempStream, line);
                writeLine(tempStream, nextLine);
                i++;
              } else {
                LOGGER.error(ERROR_MESSAGE,
                    "Interface declaration missing serviceGroup and interfaceName.",
                    file.getAbsolutePath(), i);
                writeLine(tempStream, line);
              }
            }
            continue;
          }
          writeLine(tempStream, line);
        }
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception e) {
        LOGGER.error("Process file [{}] failed", file.getAbsolutePath(), e);
      }
    });
  }

  // FeignClient属性信息
  private String feignClientInfo(String definitionLine, String interfaceName, String className) {
    interfaceName = interfaceName.toLowerCase(Locale.ROOT).substring(0, 1) + interfaceName.substring(1);
    Matcher matcher = HSF_SERVICE_GROUP.matcher(definitionLine);
    String serviceName = null;
    if (matcher.find()) {
      serviceName = matcher.group();
      serviceName = serviceName.substring(serviceName.indexOf("\"") + 1, serviceName.lastIndexOf("\""));
    }
    if (serviceName == null) {
      serviceName = "${feign.client." + className + "}";
    }
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("@FeignClient(name = \"")
        .append(serviceName)
        .append("\"")
        .append(", contextId = \"")
        .append(interfaceName.toLowerCase(Locale.ROOT).substring(0, 1) + interfaceName.substring(1))
        .append("\", ")
        .append("path = \"/")
        .append(interfaceName)
        .append("\")");
    return new String(stringBuilder);
  }

  // 接口拓展信息
  private String interfaceExtension(String line) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] s = line.trim().split(" ");
    stringBuilder.append("public interface ")
        .append(s[1])
        .append("Ext extends ")
        .append(s[1])
        .append("{}");
    return new String(stringBuilder);
  }
}
