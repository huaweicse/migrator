package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.Const;
import com.huaweicse.tools.migrator.common.FileAction;
import com.huaweicse.tools.migrator.common.ParamValueType;

/**
 * 功能描述：
 * 扫描目录下面的所有JAVA文件，首先扫描出包含 @HSFProvider 标签的文件，并从中解析出需要处理的 JAVA interface文件，
 * 然后将 interface 文件修改为 REST 风格。 替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFInterface2RestAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFInterface2RestAction.class);

  private static final String INTERFACE_REGEX_PATTERN = "[a-zA-Z]+(.class)";

  private static final String ROUTER_REGEX_PATTERN = "[/*{}]";

  private static final String HSF_PROVIDER = "@HSFProvider";

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

  private void replaceContent(List<File> acceptedFiles, List<String> interfaceFileList) {
    acceptedFiles.forEach(file -> {
      try {
        if (interfaceFileList.size() == 0) {
          return;
        }
        CharArrayWriter tempStream = new CharArrayWriter();
        String tempInterfaceFileName = null;
        for (String interfaceFileName : interfaceFileList) {
          if (interfaceFileName.equals(file.getName())) {
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            for (String line : lines) {
              if (line.contains("package")) {
                writeLine(tempStream, line);
                writeLine(tempStream, "");
                writeLine(tempStream, "import " + Const.RESPONSE_BODY_PACKAGE_NAME + ";");
                writeLine(tempStream, "import " + Const.POST_MAPPING_PACKAGE_NAME + ";");
                writeLine(tempStream, "import " + Const.REQUEST_PARAM_PACKAGE_NAME + ";");
                writeLine(tempStream, "import " + Const.REQUEST_BODY_PACKAGE_NAME + ";");
                continue;
              }
              if (!("".equals(line) || isEffectiveInterface(line))) {
                String[] strings = line.trim().replace("(", " ").split(" ");
                writeLine(tempStream, "  @ResponseBody");
                ArrayList<String> paramList = paramHandling(line, file);
                if (paramList == null) {
                  writeLine(tempStream, postMappingString(strings[0], strings[1], "0"));
                  writeLine(tempStream, line);
                } else {
                  writeLine(tempStream, postMappingString(strings[0], strings[1], paramList.get(paramList.size() - 1)));
                  writeLine(tempStream, interfaceInfo(line, paramList));
                }
                continue;
              }
              writeLine(tempStream, line);
            }
            tempInterfaceFileName = interfaceFileName;
            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
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

  private static ArrayList<String> paramHandling(String line, File file) {
    String[] tempParams = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"))
        .replaceAll(",", " ").split(" ");
    if (tempParams.length == 1) {
      return null;
    }
    int requestBodyCount = 0;
    List<String> paramStrings = Arrays.stream(tempParams)
        .filter(param -> !"".equals(param))
        .collect(Collectors.toList());
    ArrayList<String> params = new ArrayList<>(paramStrings.size());
    for (int i = 0; i < paramStrings.size(); i++) {
      String tempParam = paramStrings.get(i);
      if (i % 2 == 0) {
        if (isComplexType(tempParam)) {
          params.add("@RequestBody " + tempParam);
          requestBodyCount++;
          if (requestBodyCount >= 2) {
            String methodName = line.trim().substring(line.trim().indexOf(" ") + 1, line.trim().indexOf("("));
            LOGGER.error("RequestBody too much, need to reconstruct parameters and method name is {} of {}",
                methodName, file.getName());
          }
        } else {
          params.add("@RequestParam(value=\"" + paramStrings.get(i + 1) + "\") " + tempParam);
        }
        continue;
      }
      params.add(tempParam);
    }
    params.add(String.valueOf(requestBodyCount));
    return params;
  }

  private static boolean isComplexType(String param) {
    ParamValueType[] values = ParamValueType.values();
    for (ParamValueType value : values) {
      if (param.equalsIgnoreCase(value.name()) || "int".equals(value.name()) || "char".equals(value.name())) {
        return false;
      }
    }
    return true;
  }

  private String postMappingString(String resultTypeValue, String router, String bodyCount) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("  @PostMapping(value = \"/")
        .append(router)
        .append("\"");
    if (isComplexType(resultTypeValue)) {
      stringBuilder.append(", produces = \"x-application/hessian2\"");
    }
    if (Integer.parseInt(bodyCount) >= 1) {
      stringBuilder.append(", consumes = \"x-application/hessian2\"");
    }
    stringBuilder.append(")");
    return new String(stringBuilder);
  }

  private String interfaceInfo(String line, ArrayList<String> paramList) {
    String substring = line.substring(0, line.indexOf("("));
    if (Integer.parseInt(paramList.get(paramList.size() - 1)) > 1) {
      return line;
    }
    paramList.remove(paramList.get(paramList.size() - 1));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(substring)
        .append("(");
    for (int i = 0; i < paramList.size(); i++) {
      if (i % 2 == 0) {
        stringBuilder.append(paramList.get(i)).append(" ");
        continue;
      }
      if (i == (paramList.size() - 1)) {
        stringBuilder.append(paramList.get(i));
        continue;
      }
      stringBuilder.append(paramList.get(i)).append(", ");
    }
    stringBuilder.append(");");
    return new String(stringBuilder);
  }

  private boolean isEffectiveInterface(String line) {
    Pattern pattern = Pattern.compile(ROUTER_REGEX_PATTERN);
    Matcher matcher = pattern.matcher(line);
    return matcher.find();
  }
}

