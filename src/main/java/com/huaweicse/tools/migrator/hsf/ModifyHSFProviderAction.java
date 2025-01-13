package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
import org.springframework.util.StringUtils;

import com.huaweicse.tools.migrator.common.Const;
import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFProvider 标签，如果存在，将其替换为 @RestController。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFProviderAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFProviderAction.class);

  private static final String CLASS_NAME = "[a-zA-Z]+[a-zA-Z0-9]*";

  private static final Pattern INTERFACE_REGEX_PATTERN = Pattern.compile(CLASS_NAME + "(.class)");

  private static final String HSF_PROVIDER = "@HSFProvider";

  private static final String HSF_PROVIDER_COMMENT = "//@HSFProvider";

  private static final String CLASS_DECLARE_PREFIX = "public class ";

  private static final Pattern CLASS_DECLARE_PATTERN = Pattern.compile(CLASS_DECLARE_PREFIX + CLASS_NAME);

  private final Map<String, Set<String>> controllerPath2Beans = new HashMap<>();

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
    return fileContains(file, HSF_PROVIDER) && !fileContains(file, HSF_PROVIDER_COMMENT);
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

        if (line.contains(Const.HSF_PROVIDER_PACKAGE_NAME)) {
          line = line.replace(Const.HSF_PROVIDER_PACKAGE_NAME, Const.REQUEST_MAPPING_PACKAGE_NAME);
          writeLine(tempStream, line);
          writeLine(tempStream, "import " + Const.REST_CONTROLLER_PACKAGE_NAME + ";");
          continue;
        }
        if (line.trim().startsWith(HSF_PROVIDER)) {
          Matcher matcher = INTERFACE_REGEX_PATTERN.matcher(line);
          while (matcher.find()) {
            interfaceName = matcher.group().replace(".class", "");
          }
          if (interfaceName == null) {
            LOGGER.warn("@HSFProvicer not hava interface property in file [{}] at line {}.", file.getAbsolutePath(), i);
            interfaceName = getInterfaceNameFromImplClass(lines, file.getAbsolutePath());
            if (!StringUtils.hasLength(interfaceName)) {
              continue;
            }
          }
          writeLine(tempStream, "@org.springframework.web.bind.annotation.RestController");
          writeLine(tempStream, "@org.springframework.context.annotation.Lazy");
          writeLine(tempStream, getRequestMappingAnnotation(interfaceName, file.getAbsolutePath()));
          continue;
        }
//        // 注入的 service bean 设置为 Lazy， 避免循环依赖。
//        if (line.contains("@Autowired") || line.contains("@Resource")) {
//          String nextLine = lines.get(i + 1);
//          if (nextLine.contains(" " + interfaceName + " ")) {
//            writeLine(tempStream, line);
//            writeLine(tempStream, "    @org.springframework.context.annotation.Lazy");
//            continue;
//          }
//          if(nextLine.contains("@Qualifier")) {
//            nextLine = lines.get(i + 2);
//            if (nextLine.contains(" " + interfaceName + " ")) {
//              writeLine(tempStream, line);
//              writeLine(tempStream, "    @org.springframework.context.annotation.Lazy");
//              continue;
//            }
//          }
//        }
        writeLine(tempStream, line);
      }
      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  private String getRequestMappingAnnotation(String interfaceName, String filePath) {
    final StringBuilder requestMappingLine = new StringBuilder("@RequestMapping(\"/");
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

  private String getInterfaceNameFromImplClass(List<String> lines, String filePath) {
    final Matcher matcher = lines.stream()
        .map(CLASS_DECLARE_PATTERN::matcher)
        .filter(Matcher::find)
        .findFirst()
        .orElse(null);
    if (matcher == null) {
      LOGGER.error("Class declaration line not found in file [{}].", filePath);
      return "";
    }
    final String className = matcher.group().replace(CLASS_DECLARE_PREFIX, "");
    LOGGER.info("Recognized class name: {}", className);
    if (StringUtils.endsWithIgnoreCase(className, "impl")) {
      return className.substring(0, className.length() - 4);
    }
    return className;
  }
}