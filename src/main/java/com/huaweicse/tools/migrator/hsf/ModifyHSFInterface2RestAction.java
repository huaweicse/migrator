package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Const;
import com.huaweicse.tools.migrator.common.FileAction;
import com.huaweicse.tools.migrator.common.Parameter;

/**
 * 功能描述：
 * 扫描目录下面的所有JAVA文件，首先扫描出包含 @HSFProvider 标签的文件，并从中解析出需要处理的 JAVA interface文件，
 * 然后将 interface 文件修改为 REST 风格。 替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFInterface2RestAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFInterface2RestAction.class);

  private static final String INTERFACE_REGEX_PATTERN = "[a-zA-Z]+(.class)";

  private static final String HSF_PROVIDER = "@HSFProvider";

  private static final Pattern PATTERN_METHOD = Pattern.compile(
      "[ a-zA-Z0-9<>\\[\\],]+\\([ a-zA-Z0-9<>\\[\\],\\.]*\\);[ ]*");

  private static final Pattern PATTERN_METHOD_NAME = Pattern.compile(" [a-zA-Z0-9]+\\(");

  private static final Pattern PATTERN_METHOD_PARAMETERS = Pattern.compile("\\([ a-zA-Z0-9<>\\[\\],\\.]*\\)");

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    List<String> interfaceFileList = filterInterfaceFile(acceptedFiles);
    replaceContent(acceptedFiles, interfaceFileList);
  }

  @Override
  protected boolean isAcceptedFile(File file) {
    return file.getName().endsWith(".java");
  }

  private List<String> filterInterfaceFile(List<File> acceptedFiles) throws IOException {
    List<String> interfaceFileList = new ArrayList<>();
    for (File file : acceptedFiles) {
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
    }
    return interfaceFileList;
  }

  private void replaceContent(List<File> acceptedFiles, List<String> interfaceFileList) throws Exception {
    String fileName;

    for (File file : acceptedFiles) {
      fileName = file.getAbsolutePath();

      if (!interfaceFileList.contains(file.getName())) {
        continue;
      }

      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      if (!isInterfaceFile(lines)) {
        continue;
      }

      CharArrayWriter tempStream = new CharArrayWriter();
      boolean notesBegin = false;
      int lineNumber = 0;
      for (String line : lines) {
        lineNumber++;
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

        if (line.startsWith("package ")) {
          writeLine(tempStream, line);
          writeLine(tempStream, "");
          writeLine(tempStream, "import " + Const.RESPONSE_BODY_PACKAGE_NAME + ";");
          writeLine(tempStream, "import " + Const.POST_MAPPING_PACKAGE_NAME + ";");
          writeLine(tempStream, "import " + Const.REQUEST_PARAM_PACKAGE_NAME + ";");
          writeLine(tempStream, "import " + Const.REQUEST_HEADER_PACKAGE_NAME + ";");
          writeLine(tempStream, "import " + Const.REQUEST_BODY_PACKAGE_NAME + ";");
          continue;
        }

        if (isMethod(line)) {
          writeLine(tempStream, "    @ResponseBody");
          String methodName = methodName(line);
          Parameter[] parameters = methodParameters(line, fileName, lineNumber);
          writeLine(tempStream, "    @PostMapping(value = \"/" + methodName + "\""
              + ", produces = \"x-application/hessian2\""
              + ", consumes = \"x-application/hessian2\""
              + ")");
          writeLine(tempStream, line.substring(0, line.indexOf("(") + 1)
              + buildParameters(parameters, fileName, lineNumber) + ");");
          continue;
        }

        writeLine(tempStream, line);
      }

      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  public static String buildParameters(Parameter[] parameters, String fileName, int lineNumber) {
    StringBuilder result = new StringBuilder();
    int bodyCount = 0;
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      if (i > 0) {
        result.append(", ");
      }
      if (parameter.isSimpleType()) {
        result.append("@RequestParam(value=\"" + parameter.name + "\") " + parameter.type + " " + parameter.name);
      } else if (parameter.isStringType()) {
        result.append("@RequestHeader(value=\"" + parameter.name + "\") " + parameter.type + " " + parameter.name);
      } else {
        result.append("@RequestBody " + parameter.type + " " + parameter.name);
        bodyCount++;
      }
    }
    if (bodyCount > 1) {
      LOGGER.error("File has too many body parameters {} {}.", fileName, lineNumber);
    }
    return result.toString();
  }

  public static Parameter[] methodParameters(String line, String fileName, int lineNumber) {
    Matcher matcher = PATTERN_METHOD_PARAMETERS.matcher(line);
    if (matcher.find()) {
      String name = matcher.group();
      name = name.substring(1, name.length() - 1).trim();
      if (name.isEmpty()) {
        return new Parameter[0];
      }
      String[] pairs = methodParametersTokens(name);
      if (pairs.length % 2 != 0) {
        throw new IllegalStateException("wrong method detected " + fileName + " " + lineNumber);
      }
      Parameter[] result = new Parameter[pairs.length / 2];
      for (int i = 0; i < pairs.length; i = i + 2) {
        result[i / 2] = new Parameter(pairs[i].trim(), pairs[i + 1].trim());
      }
      return result;
    }
    throw new IllegalStateException("wrong method detected " + line);
  }

  public static String[] methodParametersTokens(String line) {
    List<String> tokens = new ArrayList<>();
    StringBuilder token = new StringBuilder();
    line = line.trim();
    char[] chars = line.toCharArray();
    boolean genericBegin = false;
    for (char c : chars) {
      if (c == '<') {
        genericBegin = true;
      }
      if (c == '>') {
        genericBegin = false;
      }
      if (genericBegin) {
        token.append(c);
      } else {
        if (c == ' ' || c == ',') {
          if (token.length() > 0) {
            tokens.add(token.toString());
            token.setLength(0);
          }
        } else {
          token.append(c);
        }
      }
    }
    if (token.length() > 0) {
      tokens.add(token.toString());
      token.setLength(0);
    }
    return tokens.toArray(new String[0]);
  }

  public static String methodName(String line) {
    Matcher matcher = PATTERN_METHOD_NAME.matcher(line);
    if (matcher.find()) {
      String name = matcher.group();
      return name.substring(1, name.length() - 1);
    }
    throw new IllegalStateException("wrong method detected " + line);
  }

  public static boolean isMethod(String line) {
    return PATTERN_METHOD.matcher(line).matches();
  }

  private boolean isInterfaceFile(List<String> lines) {
    for (String line : lines) {
      if (line.startsWith("public interface") || line.startsWith("interface")) {
        return true;
      }
    }
    return false;
  }
}

