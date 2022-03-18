package com.huaweicse.tools.migrator.dubbo;

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

import com.huaweicse.tools.migrator.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @DubboService 标签，如果存在，将其替换为 @RestController。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyDubboServiceAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyDubboServiceAction.class);

  @Value("${dubbo.provider.packageName:org.apache.dubbo.config.annotation.DubboService}")
  private String dubboProviderPackageName;

  @Value("${spring.requestMapping.packageName:org.springframework.web.bind.annotation.RequestMapping}")
  private String requestMappingPackageName;

  @Value("${spring.restController.packageName:org.springframework.web.bind.annotation.RestController}")
  private String restControllerPackageName;

  private static final String LINE_SEPARATOR = "line.separator";

  private static final String INTERFACE_REGEX_PATTERN = "implements [a-zA-Z][a-zA-Z0-9]*";

  private static final String DUBBO_SERVICE = "@DubboService";

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
    return fileContains(file, DUBBO_SERVICE);
  }

  private void replaceContent(List<File> acceptedFiles) throws IOException {
    for (File file : acceptedFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      CharArrayWriter tempStream = new CharArrayWriter();
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);

        if (line.contains(dubboProviderPackageName)) {
          line = line.replace(dubboProviderPackageName, requestMappingPackageName);
          tempStream.write(line);
          tempStream.append(System.getProperty(LINE_SEPARATOR));
          tempStream.write("import " + restControllerPackageName + ";");
          tempStream.append(System.getProperty(LINE_SEPARATOR));
          continue;
        }
        if (line.trim().startsWith(DUBBO_SERVICE)) {
          Pattern pattern = Pattern.compile(INTERFACE_REGEX_PATTERN);
          String nextLine = lines.get(i + 1);
          Matcher matcher = pattern.matcher(nextLine);
          String interfaceName = null;
          while (matcher.find()) {
            interfaceName = matcher.group().replace("implements ", "");
          }

          if (interfaceName == null) {
            LOGGER.error(ERROR_MESSAGE, "@DubboSerivce not follow interface definition.", file.getAbsolutePath(),
                i);
            continue;
          }

          tempStream.write("@RestController");
          tempStream.append(System.getProperty(LINE_SEPARATOR));
          tempStream.write(
              "@RequestMapping(\"/" + interfaceName.substring(0, 1).toLowerCase() + interfaceName.substring(1)
                  + "\")");
          tempStream.append(System.getProperty(LINE_SEPARATOR));
          continue;
        }
        tempStream.write(line);
        tempStream.append(System.getProperty(LINE_SEPARATOR));
      }
      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }
}