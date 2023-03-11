package com.huaweicse.tools.migrator.hsf;

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
import org.springframework.stereotype.Component;

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

  private static final String INTERFACE_REGEX_PATTERN = "[a-zA-Z]+[a-zA-Z0-9]*(.class)";

  private static final String HSF_PROVIDER = "@HSFProvider";

  private static final String HSF_PROVIDER_COMMENT = "//@HSFProvider";

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
          Pattern pattern = Pattern.compile(INTERFACE_REGEX_PATTERN);
          Matcher matcher = pattern.matcher(line);
          while (matcher.find()) {
            interfaceName = matcher.group().replace(".class", "");
          }
          if (interfaceName == null) {
            LOGGER.error(ERROR_MESSAGE, "@HSFProvicer not hava interface property.",
                file.getAbsolutePath(),
                i);
            continue;
          }
          writeLine(tempStream, "@RestController");
          writeLine(tempStream, "@org.springframework.context.annotation.Lazy");
          writeLine(tempStream,
              "@RequestMapping(\"/" + interfaceName.substring(0, 1).toLowerCase() + interfaceName.substring(1) + "\")");
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
}