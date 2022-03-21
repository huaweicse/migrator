package com.huaweicse.tools.migrator.dubbo;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class ModifyDubboReferenceAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyDubboReferenceAction.class);

  @Value("${dubbo.dubboReference.packageName:org.apache.dubbo.config.annotation.DubboReference}")
  private String dubboReferencePackageName;

  @Value("${spring.autowired.packageName:org.springframework.beans.factory.annotation.Autowired}")
  private String autowiredPackageName;

  private static final String DUBBO_REFERENCE = "@DubboReference";

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
    return fileContains(file, DUBBO_REFERENCE);
  }

  private void replaceContent(List<File> acceptedFiles) {
    for (File file : acceptedFiles) {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (String line : lines) {
          if (line.startsWith("import") && line.contains(dubboReferencePackageName)) {
            line = line.replace(dubboReferencePackageName, autowiredPackageName);
            tempStream.write(line);
            tempStream.append(LINE_SEPARATOR);
            continue;
          }
          if (line.contains(DUBBO_REFERENCE)) {
            line = line.replace(DUBBO_REFERENCE, "@Autowired");
            tempStream.write(line);
            tempStream.append(LINE_SEPARATOR);
            continue;
          }
          tempStream.write(line);
          tempStream.append(LINE_SEPARATOR);
        }
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception e) {
        LOGGER.error("Process file [{}] failed", file.getAbsolutePath(), e);
      }
    }
  }
}
