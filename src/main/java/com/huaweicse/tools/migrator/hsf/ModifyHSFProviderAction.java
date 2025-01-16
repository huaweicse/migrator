package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFProvider 标签，如果存在，将其替换为 @RestController。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFProviderAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFProviderAction.class);

  private static final Pattern CLASS_IMPLEMENT_PATTERN = Pattern.compile(" implements\\s+[a-zA-Z]+[a-zA-Z0-9]*");

  private final Map<String, Set<String>> controllerPath2Beans = new HashMap<>();

  @Override
  public void run(String... args) throws Exception {
    ReadHSFInfoAction action = new ReadHSFInfoAction();
    action.run(args[0]);
    List<File> acceptedFiles = acceptedFiles(args[0]);
    acceptedFiles = filterFiles(action.getImplementationNames(), acceptedFiles);
    replaceContent(acceptedFiles);
  }

  private List<File> filterFiles(List<String> implementationNames, List<File> acceptedFiles) {
    final Set<String> names = new HashSet<>(implementationNames.size());
    for (String item : implementationNames) {
      names.add(item.substring(item.lastIndexOf(".") + 1) + ".java");
    }
    List<File> result = new ArrayList<>(acceptedFiles.size());
    for (File file : acceptedFiles) {
      if (names.contains(file.getName())) {
        result.add(file);
      }
    }
    return result;
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, " implements ");
  }

  private void replaceContent(List<File> acceptedFiles) throws IOException {
    for (File file : acceptedFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      CharArrayWriter tempStream = new CharArrayWriter();
      boolean notesBegin = false;
      String interfaceName = null;
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
        if (line.trim().contains("*/")) {
          notesBegin = false;
          writeLine(tempStream, line);
          continue;
        }
        if (notesBegin) {
          writeLine(tempStream, line);
          continue;
        }
        if (line.trim().contains("/**")) {
          notesBegin = true;
          writeLine(tempStream, line);
          continue;
        }

        if (line.contains("public class ")) {
          Matcher matcher = CLASS_IMPLEMENT_PATTERN.matcher(line + lines.get(i + 1));
          if (matcher.find()) {
            interfaceName = matcher.group().substring(" implements ".length()).trim();
          } else {
            LOGGER.error("Class do not implement interface {}", file);
            continue;
          }
          writeLine(tempStream, "@org.springframework.web.bind.annotation.RestController");
          writeLine(tempStream, "@org.springframework.context.annotation.Lazy");
          writeLine(tempStream, getRequestMappingAnnotation(interfaceName, file.getAbsolutePath()));
          writeLine(tempStream, line);
          continue;
        }
        writeLine(tempStream, line);
      }
      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  private String getRequestMappingAnnotation(String interfaceName, String filePath) {
    final StringBuilder requestMappingLine = new StringBuilder(
        "@org.springframework.web.bind.annotation.RequestMapping(\"/");
    final String path = interfaceName.substring(0, 1).toLowerCase() + interfaceName.substring(1);
    requestMappingLine.append(path).append("\")");
    final Set<String> beans = controllerPath2Beans.computeIfAbsent(path, k -> new HashSet<>());
    beans.add(filePath);
    if (beans.size() > 1) {
      LOGGER.error("Controller requestMapping duplicate for path [{}], controllers: {}", path, beans);
      requestMappingLine.append(" // TODO WARNING: duplicated path.");
    }
    return requestMappingLine.toString();
  }
}