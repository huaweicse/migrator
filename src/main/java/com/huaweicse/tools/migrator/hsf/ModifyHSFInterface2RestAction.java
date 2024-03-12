package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  private static final String INTERFACE_REGEX_PATTERN = "[a-zA-Z]+[\\w]*(.class)";

  private static final String HSF_PROVIDER = "@HSFProvider";

  private static final Pattern PATTERN_METHOD = Pattern.compile(
      ".*\\s+[\\w]+\\([^()]*\\).*(;|\\{)[\\s]*");

  private static final Pattern PATTERN_API_OPERATION = Pattern.compile(
      "\\s*@ApiOperation\\(value\\s*=\\s*\\\"[^\\\"]*\\\"");

  private static final Pattern PATTERN_METHOD_NAME = Pattern.compile(" [\\w]+\\(");

  private static final Pattern PATTERN_METHOD_PARAMETERS = Pattern.compile("\\(.*\\)");

  private static final Pattern PATTERN_METHOD_PARAMETER_DEF = Pattern.compile(
      "(@\\w+)*\\s*"
          + "(([\\w.\\[\\]]+)|"
          + "([\\w.\\[\\]]+<[^>]*>)|"
          + "([\\w.\\[\\]]+<[^<]*<[^<]*>>)|"
          + "([\\w.\\[\\]]+<[^<]*<[^<]*<[^<]*>>>))"
          + " [\\w]+\\s*(,|$)");

  private static final Pattern PATTERN_METHOD_PARAMETER_TYPE_NAME = Pattern.compile(
      "(([\\w.\\[\\]]+)|"
          + "([\\w.\\[\\]]+<[^>]*>)|"
          + "([\\w.\\[\\]]+<[^<]*<[^<]*>>)|"
          + "([\\w.\\[\\]]+<[^<]*<[^<]*<[^<]*>>>))"
          + " [\\w]+\\s*(,|$)");

  private static final Pattern PATTERN_METHOD_PARAMETER_NAME = Pattern.compile(" [\\w]+$");

  private static final Pattern PATTER_INTERFACE = Pattern.compile("public interface.*|interface.*");

  private static final Pattern PATTER_INTERFACE_EXTENDS = Pattern.compile("extends\\s+[a-zA-Z]+[a-zA-Z0-9]*");

  // 无法扫描到的基类
  private static final List<String> ACCEPT_FILES = Arrays.asList(
      // support
      "ISupportImportAndExport.java", "IImportAndExportListener.java",
      "IBaseService.java", "IE3BaseService.java", "ISupportCopyService.java", "IORMService.java",
      "IShopService.java", "IAdministrationAreaService.java", "IChannelService.java", "ISupplierService.java",
      "IWareHouseService.java", "IExtendAttributeService.java", "ILoginService.java", "ISystemParameterComService.java",
      "ISupStrategyRelationshipService.java", "ISupStrategyService.java",
      "ISapDataQueryService.java",
      //settlement
      "ILogisticsCostReconciliateService.java",
      // goods
      "IGoodsService.java", "IE3AttributeTemplateService.java",
      // stock
      "IOneE3BaseService.java", "IE3BaseEndpointService.java"
  );

  private static Set<String> URL_NAMES = new HashSet<>();

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    List<String> interfaceFileList = filterInterfaceFile(acceptedFiles);
    interfaceFileList.addAll(ACCEPT_FILES);
    replaceContent(acceptedFiles, interfaceFileList);
  }

  @Override
  protected boolean isAcceptedFile(File file) {
    return file.getName().endsWith(".java") && fileContains(file, " interface ");
  }

  private List<String> filterInterfaceFile(List<File> acceptedFiles) throws IOException {
    List<String> interfaceFileList = new ArrayList<>();
    for (File file : acceptedFiles) {
      interfaceFileList.add(file.getName());
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
        LOGGER.error("should be an interface file {}", fileName);
        continue;
      }

      checkAndLogMultiLines(lines, fileName);

      URL_NAMES.clear();

      CharArrayWriter tempStream = new CharArrayWriter();
      boolean notesBegin = false;
      for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
        String line = lines.get(lineNumber);
        // 空行
        if (line.trim().isEmpty()) {
          writeLine(tempStream, line);
          continue;
        }
        if (line.contains("//tool ignore")) {
          notesBegin = true;
          writeLine(tempStream, line);
          continue;
        }
        if (line.contains("//end tool ignore")) {
          notesBegin = false;
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

        if (isInterfaceLine(line) && line.contains("extends")) {
          Matcher matcher = PATTER_INTERFACE_EXTENDS.matcher(line.substring(line.lastIndexOf("extends")));
          if (matcher.find()) {
            String extendsName = matcher.group();
            extendsName = extendsName.substring("extends".length() + 1).trim();
            if (!interfaceFileList.contains(extendsName + ".java")) {
              LOGGER.error("interface not add to list [{}] {} {}", extendsName, fileName, lineNumber);
            }
          } else {
            LOGGER.error("invalid interface detected {}", fileName, lineNumber);
          }
        }

        if (line.matches(".*\\(.*\\(.*")) {
          LOGGER.error("need check this method and can not process, {} {}", fileName, lineNumber);
        }

        if (isMethod(line)) {
          writeLine(tempStream, "    @ResponseBody");
          String methodName = methodName(line, lines.get(lineNumber - 1), fileName, lineNumber);
          Parameter[] parameters = methodParameters(line, fileName, lineNumber);
          if (parameters == null) {
            continue;
          }
          writeLine(tempStream, "    @PostMapping(value = \"/" + methodName + "\""
              + ", produces = \"x-application/hessian2\""
              + ", consumes = \"x-application/hessian2\""
              + ")");
          tempStream.write(line.substring(0, line.indexOf("(") + 1));
          tempStream.write(buildParameters(parameters, fileName, lineNumber));
          writeLine(tempStream, line.substring(line.indexOf(")")));
          continue;
        }

        //检测是否有方法换行, 如果有合并为一行处理
        if ((line.contains("(") && !line.contains(")"))
            && (lines.get(lineNumber + 1).contains(")") && !lines.get(lineNumber + 1).contains("("))) {
          if (isMethod(line + lines.get(lineNumber + 1))) {
            line = line + lines.get(lineNumber + 1);
            writeLine(tempStream, "    @ResponseBody");
            String methodName = methodName(line, lines.get(lineNumber - 1), fileName, lineNumber);
            Parameter[] parameters = methodParameters(line, fileName, lineNumber);
            if (parameters == null) {
              continue;
            }
            writeLine(tempStream, "    @PostMapping(value = \"/" + methodName + "\""
                + ", produces = \"x-application/hessian2\""
                + ", consumes = \"x-application/hessian2\""
                + ")");
            tempStream.write(line.substring(0, line.indexOf("(") + 1));
            tempStream.write(buildParameters(parameters, fileName, lineNumber));
            writeLine(tempStream, line.substring(line.indexOf(")")));
            lineNumber++;
            continue;
          }
        }

        writeLine(tempStream, line);
      }

      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  private void checkAndLogMultiLines(List<String> lines, String fileName) {
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.contains("(") && !line.contains(")")) {
        String nextLine = lines.get(i + 1);
        if (nextLine.contains(")") && !nextLine.contains("(")) {
          continue;
        }
        LOGGER.error("File may contains multiple line methods {} {}", fileName, i);
      }
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
      if (parameter.annotation != null && !parameter.annotation.isEmpty()) {
        result.append(parameter.annotation + " ");
      }
      if (parameter.isSimpleType()) {
        result.append("@RequestParam(required = false, value=\"" + parameter.name + "\") " + parameter.type + " "
            + parameter.name);
      } else if (parameter.isStringType()) {
        result.append("@RequestHeader(required = false, value=\"" + parameter.name + "\") " + parameter.type + " "
            + parameter.name);
      } else {
        result.append("@RequestBody(required = false) " + parameter.type + " " + parameter.name);
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
      name = name.replaceAll("\\s+", " ");
      return methodParametersTokens(name, fileName, lineNumber);
    }
    LOGGER.error("wrong method detected " + fileName + " " + lineNumber);
    return null;
  }

  public static Parameter[] methodParametersTokens(String line, String fileName, int lineNumber) {
    List<Parameter> parameters = new ArrayList<>();
    Matcher param = PATTERN_METHOD_PARAMETER_DEF.matcher(line);
    while (param.find()) {
      String paramDef = param.group();
      Matcher paramDefMatcher = PATTERN_METHOD_PARAMETER_TYPE_NAME.matcher(paramDef);
      if (paramDefMatcher.find()) {
        String typeAndName = paramDefMatcher.group();
        if (typeAndName.endsWith(",")) {
          typeAndName = typeAndName.substring(0, typeAndName.length() - 1);
        }
        typeAndName = typeAndName.trim();
        Matcher nameMatcher = PATTERN_METHOD_PARAMETER_NAME.matcher(typeAndName);
        if (nameMatcher.find()) {
          String name = nameMatcher.group();
          String annotation = paramDef.substring(0, paramDef.lastIndexOf(typeAndName)).trim();
          String type = typeAndName.substring(0, typeAndName.lastIndexOf(name)).trim();
          name = name.trim();
          parameters.add(new Parameter(annotation, type, name));
        } else {
          LOGGER.error("wrong method detected " + fileName + " " + lineNumber);
        }
      } else {
        LOGGER.error("wrong method detected " + fileName + " " + lineNumber);
      }
    }
    return parameters.toArray(new Parameter[0]);
  }

  public static String methodName(String line, String previousLine, String fileName, int lineNumber) {
    Matcher operation = PATTERN_API_OPERATION.matcher(previousLine);
    if (operation.find()) {
      String name = operation.group();
      return checkName(name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\"")), fileName, lineNumber);
    }
    Matcher matcher = PATTERN_METHOD_NAME.matcher(line);
    if (matcher.find()) {
      String name = matcher.group();
      return checkName(name.substring(1, name.length() - 1), fileName, lineNumber);
    }
    throw new IllegalStateException("wrong method detected " + line);
  }

  private static String checkName(String name, String fileName, int lineNumber) {
    if (URL_NAMES.contains(name)) {
      LOGGER.error("override method detected " + fileName + " " + lineNumber);
    }
    URL_NAMES.add(name);
    return name;
  }

  public static boolean isMethod(String line) {
    return PATTERN_METHOD.matcher(line).matches();
  }

  private boolean isInterfaceFile(List<String> lines) {
    for (String line : lines) {
      if (isInterfaceLine(line)) {
        return true;
      }
    }
    return false;
  }

  private boolean isInterfaceLine(String line) {
    if (PATTER_INTERFACE.matcher(line).matches()) {
      return true;
    }
    return false;
  }
}

