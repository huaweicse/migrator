package com.huaweicse.tools.migrator.dubbo;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.yaml.snakeyaml.Yaml;

import com.huaweicse.tools.migrator.common.Const;
import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @DubboReference 标签，如果存在，将其替换为 @FeignClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyDubboReferenceAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyDubboReferenceAction.class);

  private static final String INTERFACE_REGEX_PATTERN = "implements [a-zA-Z][a-zA-Z0-9]*";

  private static final String DUBBO_REFERENCE = "@DubboReference";

  private static final String DUBBO_SERVICE = "@DubboService";

  private Map<String, Set<String>> interfaceDataMap = new HashMap<>();

  private Map<String, String> microserviceNameDataMap = new HashMap<>();

  private List<File> interfaceImplFileList = new ArrayList<>();

  private List<String> interfaceData = new ArrayList<>();

  private Set<File> feignFileDirs = new HashSet<>();

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    List<File> xmlFiles = acceptedFiles.stream().filter(file -> file.getName().endsWith(".xml"))
        .collect(Collectors.toList());
    if (!ObjectUtils.isEmpty(xmlFiles)) {
      parseXmlGetMicroserviceAndInterfaceInfo(xmlFiles);
      acceptedFiles.removeAll(xmlFiles);
      modifyInterfaceImplFileContent();
    } else {
      List<File> interfaceExposeFiles = new ArrayList<>();
      for (File file : acceptedFiles) {
        if (file.getName().endsWith("java") && fileContains(file, DUBBO_SERVICE)) {
          interfaceExposeFiles.add(file);
        }
      }
      targetInterfaceInfo(interfaceExposeFiles);
      acceptedFiles.removeAll(interfaceExposeFiles);
      List<File> resourceFile = acceptedFiles.stream()
          .filter(file -> ((file.getName().endsWith(".yml")) || file.getName().endsWith(".properties")))
          .collect(Collectors.toList());
      loadResourceFile(resourceFile);
      acceptedFiles.removeAll(resourceFile);
      replaceContent(acceptedFiles);
    }
    feignClientConfigDirPath(filterFeignFile(acceptedFiles));
    writeFeignClientInfoToFile();
  }

  private List<File> filterFeignFile(List<File> acceptedFiles) throws IOException {
    List<File> tempFeignFileList = new ArrayList<>();
    for (String key : interfaceDataMap.keySet()) {
      flag:
      for (File acceptedFile : acceptedFiles) {
        if (acceptedFile.getAbsolutePath().contains(key)) {
          Set<String> values = interfaceDataMap.get(key);
          for (String value : values) {
            String[] splits = value.split(":");
            String contents = FileUtils.readFileToString(acceptedFile, StandardCharsets.UTF_8);
            if (contents.contains(splits[0]) && contents.contains(splits[1])) {
              tempFeignFileList.add(acceptedFile);
              break flag;
            }
          }
        }
      }
    }
    return tempFeignFileList;
  }

  private void parseXmlGetMicroserviceAndInterfaceInfo(List<File> xmlFiles) {
    xmlFiles.forEach(file -> {
      try {
        SAXReader saxReader = new SAXReader();
        Element rootElement = saxReader.read(file).getRootElement();
        String microserviceName = rootElement.element("application").attribute("name").getValue();
        String baseSubPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf("resources"));
        microserviceNameDataMap.putIfAbsent(baseSubPath, microserviceName);
        Iterator interfaceServices = rootElement.elementIterator("service");
        while (interfaceServices.hasNext()) {
          Element next = (Element) interfaceServices.next();
          String packageName = next.attribute("interface").getValue();
          interfaceData
              .add(String.format("%s%s", baseSubPath, packageName.substring(packageName.lastIndexOf(".") + 1)));
        }
        Iterator interfaceImplList = rootElement.elementIterator("bean");
        while (interfaceImplList.hasNext()) {
          Element next = (Element) interfaceImplList.next();
          interfaceImplFileList
              .add(new File(String.format("%s%s%s%s%s", baseSubPath, FILE_SEPARATOR, "java", FILE_SEPARATOR,
                  next.attribute("class").getValue().replace(".", FILE_SEPARATOR))));
        }
        Iterator referenceInterfaceList = rootElement.elementIterator("reference");
        while (referenceInterfaceList.hasNext()) {
          Element next = (Element) referenceInterfaceList.next();
          String packageName = next.attribute("interface").getValue();
          String interfaceInfo = packageName + ":" + packageName.substring(packageName.lastIndexOf(".") + 1);
          if (interfaceDataMap.containsKey(baseSubPath)) {
            interfaceDataMap.get(baseSubPath).add(interfaceInfo);
          } else {
            Set<String> dataSet = new HashSet<>();
            dataSet.add(interfaceInfo);
            interfaceDataMap.put(baseSubPath, dataSet);
          }
        }
      } catch (DocumentException e) {
        LOGGER.error("Process xml file [{}] failed", file.getAbsolutePath(), e);
      }
    });
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (file.getName().endsWith(".java") || file.getName().endsWith(".yml") || file.getName().endsWith(".properties")
        || (file.getName().endsWith(".xml") && !"pom.xml".equals(file.getName()))) {
      return file.getName().endsWith(".java") ?
          (fileContains(file, DUBBO_SERVICE) || fileContains(file, DUBBO_REFERENCE) || fileContains(file, "@Autowired"))
          : fileContains(file, "dubbo");
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
          if (line.contains(Const.DUBBO_REFERENCE_PACKAGE_NAME)) {
            line = line.replace(Const.DUBBO_REFERENCE_PACKAGE_NAME, Const.AUTOWIRED_PACKAGE_NAME);
            writeLine(tempStream, line);
            continue;
          }
          writeLine(tempStream, line);
          String importPackageName = line.trim().split(" ")[1].replace(";", "");
          importPackageDataList.add(importPackageName);
          continue;
        }
        if (line.trim().startsWith(DUBBO_REFERENCE)) {
          line = line.replace(DUBBO_REFERENCE, "@Autowired");
          writeLine(tempStream, line);
          String nextLine = lines.get(i + 1);
          String[] s = nextLine.trim().split(" ");
          String interfaceName = Character.isUpperCase(s[0].charAt(0)) ? s[0] : s[1];
          List<String> targetInterfacePackageName = importPackageDataList.stream()
              .filter(packageName -> packageName.contains(interfaceName)).collect(Collectors.toList());
          if (targetInterfacePackageName.size() > 1) {
            LOGGER.error(ERROR_MESSAGE, "interface packageName extraction failed", file.getAbsolutePath(), i + 1);
          }
          String value = targetInterfacePackageName.get(0) + ":" + interfaceName;
          if (interfaceDataMap.containsKey(path)) {
            interfaceDataMap.get(path).add(value);
          } else {
            Set<String> dataSet = new HashSet<>();
            dataSet.add(value);
            interfaceDataMap.put(path, dataSet);
          }
          i++;
          writeLine(tempStream, nextLine);
          continue;
        }
        writeLine(tempStream, line);
      }
      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  private void modifyInterfaceImplFileContent() throws IOException {
    for (File implFile : interfaceImplFileList) {
      String interfaceImplName = implFile.getAbsolutePath()
          .substring(implFile.getAbsolutePath().lastIndexOf(FILE_SEPARATOR) + 1);
      String interfaceName = interfaceImplName.substring(0, interfaceImplName.length() - 4);
      File file = new File(implFile.getAbsolutePath().concat(".java"));
      CharArrayWriter tempStream = new CharArrayWriter();
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      for (String line : lines) {
        if (line.contains(String.format("%s implements %s", interfaceImplName, interfaceName))) {
          writeLine(tempStream, "import " + Const.REST_CONTROLLER_PACKAGE_NAME + ";");
          writeLine(tempStream, "import " + Const.REQUEST_MAPPING_PACKAGE_NAME + ";");
          writeLine(tempStream, "");
          writeLine(tempStream, "@RestController");
          String router = interfaceName.substring(0, 1).toLowerCase() + interfaceName.substring(1);
          writeLine(tempStream, String.format("@RequestMapping(\"/%s\")", router));
          writeLine(tempStream, line);
          continue;
        }
        writeLine(tempStream, line);
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
            .replace(FILE_SEPARATOR, ".");
        Set<String> interfaceSets = interfaceDataMap
            .get(feignDir.getAbsolutePath().substring(0, feignDir.getAbsolutePath().indexOf("java")));
        CharArrayWriter tempStream = new CharArrayWriter();
        writeLine(tempStream, String.format("package %s%s", packageName, ";"));
        writeLine(tempStream, "");
        for (String interfaceInfo : interfaceSets) {
          writeLine(tempStream, String.format("import %s%s", interfaceInfo.split(":")[0], ";"));
        }
        writeLine(tempStream, "");
        writeLine(tempStream, String.format("import %s%s", Const.FEIGN_CLIENT_PACKAGE_NAME, ";"));
        writeLine(tempStream, String.format("import %s%s", Const.CONFIGURATION_PACKAGE_NAME, ";"));
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
            if (data.substring(data.lastIndexOf(FILE_SEPARATOR) + 1).equals(interfaceName)) {
              String microserviceName = microserviceNameDataMap
                  .get(data.substring(0, data.lastIndexOf(FILE_SEPARATOR) + 1));
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
        File targetFile = new File(feignDir.getAbsolutePath() + FILE_SEPARATOR + "DubboInterfaceConfig.java");
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

  private void feignClientConfigDirPath(List<File> acceptedFiles) {
    acceptedFiles.forEach(file -> {
      String tempPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(FILE_SEPARATOR));
      String targetFileDir = tempPath.substring(0, tempPath.lastIndexOf(FILE_SEPARATOR) + 1) + "config";
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
        String microserviceName = null;
        if (file.getName().endsWith(".yml")) {
          Map<String, Object> map = new Yaml().loadAs(inputStream, Map.class);
          microserviceName = (String) Objects
              .requireNonNull(initYml(Objects.requireNonNull(initYml(map, "dubbo")), "application")).get("name");
        }
        if (file.getName().endsWith(".properties")) {
          Properties properties = new Properties();
          properties.load(inputStream);
          microserviceName = properties.getProperty("dubbo.application.name");
        }
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
