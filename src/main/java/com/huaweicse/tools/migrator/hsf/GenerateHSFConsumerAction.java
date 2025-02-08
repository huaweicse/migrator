package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class GenerateHSFConsumerAction extends FileAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(GenerateHSFConsumerAction.class);

  private static final String HSF_CONSUMER_TAG = "hsf:consumer";

  private final Set<String> consumers = new HashSet<>();

  @Override
  public void run(String... args) throws Exception {
    final String rootPath = args[0];
    final String packageName = args[1];
    final String className = args[2];
    CharArrayWriter tempStream = new CharArrayWriter();

    writeLine(tempStream, "package " + packageName + ";");
    writeLine(tempStream, "");

    writeLine(tempStream, "import org.springframework.cloud.openfeign.FeignClient;");
    writeLine(tempStream, "import org.springframework.context.annotation.Configuration;");
    writeLine(tempStream, "");

    writeLine(tempStream, "@Configuration");
    writeLine(tempStream, "public class " + className + " {");

    final List<File> files = acceptedFiles(rootPath);
    for (File config : files) {
      LOGGER.info("Reading consumer config file {}", config);
      generateFeignConfig(config, tempStream, className);
    }
    writeLine(tempStream, "}");

    writeTargetFile(rootPath, tempStream, packageName, className);
  }

  private void generateFeignConfig(File config, CharArrayWriter tempStream, String className)
      throws ParserConfigurationException, IOException, SAXException {
    final NodeList beanLists = getBeanLists(config);
    for (int i = 0; i < beanLists.getLength(); i++) {
      Node node = beanLists.item(i);
      final NamedNodeMap attributes = node.getAttributes();
      String interfaceFullName = attributes.getNamedItem("interface").getNodeValue();
      final String interfaceName = interfaceFullName.substring(interfaceFullName.lastIndexOf(".") + 1);
      final String beanId = attributes.getNamedItem("id").getNodeValue();
      if (!beanId.equalsIgnoreCase(interfaceName)) {
        LOGGER.warn("Bean id [{}] is different from interfaceName [{}]", beanId, interfaceName);
      }
      if (!consumers.add(beanId)) {
        LOGGER.warn("Duplicate consumer found in file [{}], for consumer {}", config, interfaceFullName);
      }
      String interfaceLowerName = beanId.toLowerCase(Locale.ROOT).charAt(0) + beanId.substring(1);
      writeLine(tempStream, "    @FeignClient(name = \"${feign.client." + className + "}\",");
      writeLine(tempStream, "        contextId = \"" + interfaceLowerName
          + "\", path = \"" + "/" + interfaceLowerName + "\")");
      final String feignInterface = beanId.toUpperCase(Locale.ROOT).charAt(0) + beanId.substring(1) + "Ext";
      writeLine(tempStream, "    public interface " + feignInterface + " extends " + interfaceFullName + "{}");
      writeLine(tempStream, "");
    }
  }

  private NodeList getBeanLists(File config)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(config);

    return document.getElementsByTagName(HSF_CONSUMER_TAG);
  }

  private void writeTargetFile(String rootPath, CharArrayWriter tempStream, String packageName, String className)
      throws IOException {
    String parentPath = Paths.get(Paths.get(rootPath, "src", "main", "java").normalize().toString(),
        packageName.split("\\.")).normalize().toString();
    final File targetFile = new File(parentPath, className + ".java");
    FileUtils.createParentDirectories(targetFile);
    try (OutputStreamWriter fileWriter = new OutputStreamWriter(Files.newOutputStream(targetFile.toPath()),
        StandardCharsets.UTF_8)) {
      tempStream.writeTo(fileWriter);
    }
    tempStream.close();
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    return StringUtils.endsWithIgnoreCase(file.getName(), "xml") && fileContains(file, HSF_CONSUMER_TAG);
  }
}
