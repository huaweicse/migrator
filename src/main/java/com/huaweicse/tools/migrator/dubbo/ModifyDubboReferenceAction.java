package com.huaweicse.tools.migrator.dubbo;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @DubboReference 标签，如果存在，将其替换为 @FeignClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyDubboReferenceAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyDubboReferenceAction.class);

  @Value("${dubbo.dubboReference.packageName:org.apache.dubbo.config.annotation.DubboReference}")
  private String dubboReferencePackageName;

  @Value("${spring.autowired.packageName:org.springframework.beans.factory.annotation.Autowired}")
  private String autowiredPackageName;

  @Value("${spring.feignClient.packageName:org.springframework.cloud.openfeign.FeignClient}")
  private String feignClientPackageName;

  @Value("${spring.configuration.packageName:org.springframework.context.annotation.Configuration}")
  private String configurationPackageName;

  private static final String INTERFACE_REGEX_PATTERN = "implements [a-zA-Z][a-zA-Z0-9]*";

  private static final String DUBBO_REFERENCE = "@DubboReference";

  private static final String DUBBO_SERVICE = "@DubboService";

  private Map<String, Set<String>> interfaceDataMap = new HashMap<>();

  private Map<String, String> microserviceNameDataMap = new HashMap<>();

  private List<String> interfaceData = new ArrayList<>();

  private Set<File> feignFileDirs = new HashSet<>();

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    List<File> interfaceExposeFiles = new ArrayList<>();
    for (File file : acceptedFiles) {
      if (file.getName().endsWith("java") && fileContains(file, DUBBO_SERVICE)) {
        interfaceExposeFiles.add(file);
      }
    }
    targetInterfaceInfo(interfaceExposeFiles);
    acceptedFiles.removeAll(interfaceExposeFiles);
    List<File> resourceFile = acceptedFiles.stream()
        .filter(file -> (file.getName().endsWith(".yml"))).collect(Collectors.toList());
    loadResourceFile(resourceFile);
    acceptedFiles.removeAll(resourceFile);
    replaceContent(acceptedFiles);
    feignClientConfigDirPath(acceptedFiles);
    writeFeignClientInfoToFile();
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (file.getName().endsWith(".java") || file.getName().endsWith(".yml")) {
      return file.getName().endsWith(".java") ?
          (fileContains(file, DUBBO_SERVICE) || fileContains(file, DUBBO_REFERENCE)) : fileContains(file, "dubbo");
    }
    return false;
  }

  private void targetInterfaceInfo(List<File> interfaceExposeFiles) throws IOException {
    for (File file : interfaceExposeFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        if (line.trim().startsWith(DUBBO_SERVICE)) {
          Pattern pattern = Pattern.compile(INTERFACE_REGEX_PATTERN);
          String nextLine = lines.get(i + 1);
          Matcher matcher = pattern.matcher(nextLine);
          String interfaceName = null;
          while (matcher.find()) {
            interfaceName = matcher.group().replace("implements ", "");
          }
          if (interfaceName == null) {
            LOGGER.error(ERROR_MESSAGE, "@DubboSerivce not follow interface definition.", file.getAbsolutePath(), i);
            break;
          }
          interfaceData
              .add(file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf("java")) + interfaceName);
          break;
        }
      }
    }
  }

  private void replaceContent(List<File> acceptedFiles) throws IOException {
    for (File file : acceptedFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      CharArrayWriter tempStream = new CharArrayWriter();
      String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf("java"));
      List<String> importPackageDataList = new ArrayList<>();
      for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        if (line.startsWith("import")) {
          if (line.contains(dubboReferencePackageName)) {
            line = line.replace(dubboReferencePackageName, autowiredPackageName);
            tempStream.write(line);
            tempStream.append(LINE_SEPARATOR);
            continue;
          }
          tempStream.write(line);
          tempStream.append(LINE_SEPARATOR);
          String importPackageName = line.trim().split(" ")[1].replace(";", "");
          importPackageDataList.add(importPackageName);
          continue;
        }
        if (line.trim().startsWith(DUBBO_REFERENCE)) {
          line = line.replace(DUBBO_REFERENCE, "@Autowired");
          tempStream.write(line);
          tempStream.append(LINE_SEPARATOR);
          String nextLine = lines.get(i + 1);
          String interfaceName = nextLine.trim().split(" ")[1];
          List<String> targetInterfacePackageName = importPackageDataList.stream()
              .filter(packageName -> packageName.contains(interfaceName)).collect(Collectors.toList());
          if (targetInterfacePackageName.size() > 1) {
            LOGGER.error(ERROR_MESSAGE, "interface packageName extraction failed", file.getAbsolutePath(), i + 1);
          }
          if (interfaceDataMap.containsKey(path)) {
            interfaceDataMap.get(path).add(targetInterfacePackageName.get(0) + ":" + interfaceName);
          } else {
            interfaceDataMap.put(path, Collections.singleton(targetInterfacePackageName.get(0) + ":" + interfaceName));
          }
          i++;
          tempStream.write(nextLine);
          tempStream.append(LINE_SEPARATOR);
          continue;
        }
        tempStream.write(line);
        tempStream.append(LINE_SEPARATOR);
      }
      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  private void writeFeignClientInfoToFile() {
    feignFileDirs.forEach(feignDir -> {
      try {
        String packageName = feignDir.getAbsolutePath().substring(feignDir.getAbsolutePath().indexOf("java") + 5)
            .replace(File.separator, ".");
        Set<String> interfaceSets = interfaceDataMap
            .get(feignDir.getAbsolutePath().substring(0, feignDir.getAbsolutePath().indexOf("java")));
        CharArrayWriter tempStream = new CharArrayWriter();
        writeLine(tempStream, String.format("package %s%s", packageName, ";"));
        writeLine(tempStream, "");
        for (String interfaceInfo : interfaceSets) {
          writeLine(tempStream, String.format("import %s%s", interfaceInfo.split(":")[0], ";"));
        }
        writeLine(tempStream, "");
        writeLine(tempStream, String.format("import %s%s", feignClientPackageName, ";"));
        writeLine(tempStream, String.format("import %s%s", configurationPackageName, ";"));
        writeLine(tempStream, "");
        writeLine(tempStream, "@Configuration");
        writeLine(tempStream, "public class DubboInterfaceConfig {");
        writeLine(tempStream, "");
        for (String interfaceInfo : interfaceSets) {
          String tempInterfaceInfo = null;
          if (interfaceData.size() == 0) {
            break;
          }
          for (String data : interfaceData) {
            String interfaceName = interfaceInfo.split(":")[1];
            if (data.substring(data.lastIndexOf(File.separator) + 1).equals(interfaceName)) {
              String microserviceName = microserviceNameDataMap
                  .get(data.substring(0, data.lastIndexOf(File.separator) + 1));
              writeLine(tempStream, feignClientInfo(microserviceName,
                  interfaceName.substring(0, 1).toLowerCase() + interfaceName.substring(1)));
              writeLine(tempStream, interfaceExtension(interfaceName));
              writeLine(tempStream, "");
              tempInterfaceInfo = data;
            }
          }
          interfaceData.remove(tempInterfaceInfo);
        }
        writeLine(tempStream, "}");
        File targetFile = new File(feignDir.getAbsolutePath() + File.separator + "DubboInterfaceConfig.java");
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(targetFile),
            StandardCharsets.UTF_8);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private String feignClientInfo(String microserviceName, String commonStr) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\t@FeignClient(name = \"")
        .append(microserviceName)
        .append("\"")
        .append(", contextId = \"")
        .append(commonStr)
        .append("\", ")
        .append("path = \"/")
        .append(commonStr)
        .append("\")");
    return new String(stringBuilder);
  }

  private String interfaceExtension(String interfaceName) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\tpublic interface ")
        .append(interfaceName)
        .append("Ext extends ")
        .append(interfaceName)
        .append("{}");
    return new String(stringBuilder);
  }

  private void writeLine(CharArrayWriter tempStream, String context) throws IOException {
    tempStream.write(context);
    tempStream.append(LINE_SEPARATOR);
  }

  private void feignClientConfigDirPath(List<File> acceptedFiles) {
    acceptedFiles.forEach(file -> {
      String tempPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator));
      String targetFileDir = tempPath.substring(0, tempPath.lastIndexOf(File.separator) + 1) + "config";
      File targetFile = new File(targetFileDir);
      if (!targetFile.exists()) {
        if (targetFile.mkdirs()) {
          feignFileDirs.add(targetFile);
        }
      }
      feignFileDirs.add(targetFile);
    });
  }

  @SuppressWarnings("unchecked")
  private void loadResourceFile(List<File> resourceFile) {
    resourceFile.forEach(file -> {
      try {
        FileInputStream inputStream = new FileInputStream(file);
        Map<String, Object> map = new Yaml().loadAs(inputStream, Map.class);
        String microserviceName = (String) Objects
            .requireNonNull(initYml(Objects.requireNonNull(initYml(map, "dubbo")), "application")).get("name");
        microserviceNameDataMap
            .putIfAbsent(file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf("resources")),
                microserviceName);
        inputStream.close();
      } catch (Exception e) {
        LOGGER.error("Process file [{}] failed", file.getAbsolutePath(), e);
      }
    });
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> initYml(Map<String, Object> map, String str) {
    Map<String, Object> maps;
    Set<Entry<String, Object>> set = map.entrySet();
    for (Map.Entry<String, Object> entry : set) {
      if (entry.getKey().equals(str)) {
        return (Map<String, Object>) entry.getValue();
      }
      if (entry.getValue() instanceof Map) {
        maps = initYml((Map<String, Object>) entry.getValue(), str);
        if (maps == null) {
          continue;
        }
        return maps;
      }
    }
    return null;
  }
}
