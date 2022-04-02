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

  private void replaceContent(List<File> acceptedFiles) throws IOException {
    for (File file : acceptedFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      CharArrayWriter tempStream = new CharArrayWriter();
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        if (line.contains(Const.HSF_PROVIDER_PACKAGE_NAME)) {
          line = line.replace(Const.HSF_PROVIDER_PACKAGE_NAME, Const.REQUEST_MAPPING_PACKAGE_NAME);
          writeLine(tempStream, line);
          writeLine(tempStream, "import " + Const.REST_CONTROLLER_PACKAGE_NAME + ";");
          continue;
        }
        if (line.trim().startsWith(HSF_PROVIDER)) {
          Pattern pattern = Pattern.compile(INTERFACE_REGEX_PATTERN);
          Matcher matcher = pattern.matcher(line);
          String tempRouter = null;
          while (matcher.find()) {
            tempRouter = matcher.group().replace(".class", "");
          }
          if (tempRouter == null) {
            LOGGER.error(ERROR_MESSAGE, "@HSFProvicer not hava interface property.",
                file.getAbsolutePath(),
                i);
            continue;
          }
          writeLine(tempStream, "@RestController");
          writeLine(tempStream,
              "@RequestMapping(\"/" + tempRouter.substring(0, 1).toLowerCase() + tempRouter.substring(1) + "\")");
          continue;
        }
        writeLine(tempStream, line);
      }
      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }
}