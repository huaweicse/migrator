package com.huaweicse.tools.migrator.hsf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class ReadHSFInfoAction extends FileAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ReadHSFInfoAction.class);

  private List<String> interfaceNames = new ArrayList<>();

  private List<String> implementationNames = new ArrayList<>();

  private Map<String, String> implementationInterfaces = new HashMap<>();

  @Override
  public void run(String... args) throws Exception {
    File config = acceptedFiles(args[0]).get(0);
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(config);

    final Map<String, String> beansNames = getBeanRef2Names(document);

    NodeList bookList = document.getElementsByTagName("hsf:provider");
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
    return file.getName().equals("hsf-provider-beans.xml");
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
