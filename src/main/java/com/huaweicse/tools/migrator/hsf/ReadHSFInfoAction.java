package com.huaweicse.tools.migrator.hsf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class ReadHSFInfoAction extends FileAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReadHSFInfoAction.class);

  private static final String HSF_PROVIDER_TAG = "hsf:provider";

  private List<String> interfaceNames = new ArrayList<>();

  private List<String> implementationNames = new ArrayList<>();

  private Map<String, String> implementationInterfaces = new HashMap<>();

  @Override
  public void run(String... args) throws Exception {
    final String parentPath = args[0];
    final List<File> files = acceptedFiles(parentPath);
    if (CollectionUtils.isEmpty(files)) {
      LOGGER.error("Failed to find hsf provider bean config file from path [{}]", parentPath);
      return;
    }
    for (File configFile : files) {
      readHsfProviderConfig(configFile);
    }
  }

  private void readHsfProviderConfig(File config) throws ParserConfigurationException, SAXException, IOException {
    LOGGER.info("Found provider bean config file [{}]", config);
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(config);

    final Map<String, String> beansNames = getBeanRef2Names(document);

    NodeList bookList = document.getElementsByTagName(HSF_PROVIDER_TAG);
    for (int i = 0; i < bookList.getLength(); i++) {
      Node node = bookList.item(i);
      final String interfaceName = node.getAttributes().getNamedItem("interface").getNodeValue();
      interfaceNames.add(interfaceName);
      final String implBeanName = beansNames.get(node.getAttributes().getNamedItem("ref").getNodeValue());
      if (implBeanName == null) {
        LOGGER.error("ref do not have a bean {}", node.getAttributes().getNamedItem("ref").getNodeValue());
      }
      implementationNames.add(implBeanName);
      implementationInterfaces.put(implBeanName, interfaceName);
    }
  }

  private Map<String, String> getBeanRef2Names(Document document) {
    Map<String, String> beansNames = new HashMap<>();
    NodeList beanLists = document.getElementsByTagName("bean");
    for (int j = 0; j < beanLists.getLength(); j++) {
      final NamedNodeMap nodeAttributes = beanLists.item(j).getAttributes();
      Node beanName = nodeAttributes.getNamedItem("name");
      if (beanName == null) {
        beanName = nodeAttributes.getNamedItem("id");
      }
      beansNames.put(beanName.getNodeValue(),
          nodeAttributes.getNamedItem("class").getNodeValue());
    }
    return beansNames;
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
      return StringUtils.endsWithIgnoreCase(file.getName(), "xml") && fileContains(file, HSF_PROVIDER_TAG);
  }

  public List<String> getInterfaceNames() {
    return interfaceNames;
  }

  public List<String> getImplementationNames() {
    return implementationNames;
  }

  public Map<String, String> getImplementationInterfaces() {
    return implementationInterfaces;
  }
}
