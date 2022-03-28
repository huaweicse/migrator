package com.huaweicse.tools.migrator.eureka;

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
 *   扫描目录下面的所有JAVA文件，识别文件是否包含main函数，并将 @EnableEurekaServer 或者 @EnableEurekaClient 改为 @EnableDiscoveryClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyEurekaMainClassAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyEurekaMainClassAction.class);

  @Value("${spring.enableDiscoveryClient.packageName:org.springframework.cloud.client.discovery.EnableDiscoveryClient}")
  private String enableDiscoveryClientPackageName;

  @Value("${spring.enableEurekaClient.packageName:org.springframework.cloud.netflix.eureka.EnableEurekaClient}")
  private String enableEurekaClientPackageName;

  @Value("${spring.enableEurekaServer.packageName:org.springframework.cloud.netflix.eureka.server.EnableEurekaServer}")
  private String enableEurekaServerPackageName;

  private static final String ENABLE_EUREKA_SERVER = "@EnableEurekaClient";

  private static final String ENABLE_EUREKA_CLIENT = "@EnableEurekaServer";

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
    return fileContains(file, ENABLE_EUREKA_SERVER) || fileContains(file, ENABLE_EUREKA_CLIENT);
  }

  private void replaceContent(List<File> acceptedFiles) {
    for (File file : acceptedFiles) {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (String line : lines) {
          if (line.startsWith("import")) {
            if (line.contains(enableEurekaClientPackageName)) {
              line = line.replace(enableEurekaClientPackageName, enableDiscoveryClientPackageName);
              tempStream.write(line);
              tempStream.append(LINE_SEPARATOR);
              continue;
            }
            if (line.contains(enableEurekaServerPackageName)) {
              line = line.replace(enableEurekaServerPackageName, enableDiscoveryClientPackageName);
              tempStream.write(line);
              tempStream.append(LINE_SEPARATOR);
              continue;
            }
          }
          if (line.trim().startsWith(ENABLE_EUREKA_SERVER) || line.trim().startsWith(ENABLE_EUREKA_CLIENT)) {
            tempStream.write("@EnableDiscoveryClient");
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
