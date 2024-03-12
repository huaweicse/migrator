package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.huaweicse.tools.migrator.common.FileAction;

@Component
public class GenerateHSFConsumerAction extends FileAction {
  private String fileName;

  private String packageName;

  private String className;

  @Override
  public void run(String... args) throws Exception {
    fileName = args[1];
    packageName = args[2];
    className = args[3];

    File config = acceptedFiles(args[0]).get(0);
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    DocumentBuilder db = dbf.newDocumentBuilder();
    Document document = db.parse(config);

    NodeList beanLists = document.getElementsByTagName("hsf:consumer");
    CharArrayWriter tempStream = new CharArrayWriter();

    writeLine(tempStream, "package " + packageName + ";");
    writeLine(tempStream, "");

    writeLine(tempStream, "import org.springframework.cloud.openfeign.FeignClient;");
    writeLine(tempStream, "import org.springframework.context.annotation.Configuration;");
    writeLine(tempStream, "");

    writeLine(tempStream, "@Configuration");
    writeLine(tempStream, "public class " + className + " {");

    for (int i = 0; i < beanLists.getLength(); i++) {
      Node node = beanLists.item(i);
      String interfaceFullName = node.getAttributes().getNamedItem("interface").getNodeValue();
      String interfaceName = interfaceFullName.substring(interfaceFullName.lastIndexOf(".") + 1);
      String interfaceLowerName = interfaceName.toLowerCase(Locale.ROOT).substring(0, 1) + interfaceName.substring(1);
      writeLine(tempStream, "    @FeignClient(name = \"${feign.client." + className + "}\",");
      writeLine(tempStream, "        contextId = \"" + interfaceLowerName
          + "\", path = \"" + "/" + interfaceLowerName + "\")");
      writeLine(tempStream, "    public interface " + interfaceName + "Ext extends " + interfaceFullName + "{}");
      writeLine(tempStream, "");
    }
    writeLine(tempStream, "}");

    OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(
        new File(config.getParentFile(), className + ".java")
    ), StandardCharsets.UTF_8);
    tempStream.writeTo(fileWriter);
    fileWriter.close();
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    return file.getName().equals(fileName);
  }
}
