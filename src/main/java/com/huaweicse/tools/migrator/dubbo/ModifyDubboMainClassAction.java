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

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含main函数，并将相关 @EnableDubbo 逻辑改为 Spring Boot。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyDubboMainClassAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyDubboMainClassAction.class);

  @Value("${dubbo.enableDubbo.packageName:org.apache.dubbo.config.spring.context.annotation.EnableDubbo}")
  private String enableDubboPackageName;

  @Value("${spring.propertySource.packageName:org.springframework.context.annotation.PropertySource}")
  private String propertySourcePackageName;

  @Value("${spring.enableDiscoveryClient.packageName:org.springframework.cloud.client.discovery.EnableDiscoveryClient}")
  private String enableDiscoveryClient;

  @Value("${spring.enableFeignClients.packageName:org.springframework.cloud.openfeign.EnableFeignClients}")
  private String enableFeignClientsPackageName;

  @Value("${spring.importResource.packageName:org.springframework.context.annotation.ImportResource}")
  private String importResourcePackageName;

  private static final String ENABLE_DUBBO = "@EnableDubbo";

  private static final String IMPORT_RESOURCE = "@ImportResource";

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
    return fileContains(file, ENABLE_DUBBO) || fileContains(file, IMPORT_RESOURCE);
  }

  private void replaceContent(List<File> acceptedFiles) {
    for (File file : acceptedFiles) {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (String line : lines) {
          if (line.contains(propertySourcePackageName)) {
            continue;
          }
          if (line.startsWith("import") && (line.contains(enableDubboPackageName) || line
              .contains(importResourcePackageName))) {
            line = line.replace(enableDubboPackageName, enableDiscoveryClient);
            line = line.replace(importResourcePackageName, enableDiscoveryClient);
            tempStream.write(line);
            tempStream.append(LINE_SEPARATOR);
            tempStream.write("import " + enableFeignClientsPackageName + ";");
            tempStream.append(LINE_SEPARATOR);
            continue;
          }
          if (line.trim().startsWith("@PropertySource")) {
            continue;
          }
          if (line.trim().startsWith(ENABLE_DUBBO) || line.trim().startsWith(IMPORT_RESOURCE)) {
            tempStream.write("@EnableDiscoveryClient");
            tempStream.append(LINE_SEPARATOR);
            tempStream.write("@EnableFeignClients");
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
