package com.huaweicse.tools.migrator;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFConsumer 标签，如果存在，将其替换为 @FeignClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFConsumerAction implements Action {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFConsumerAction.class);

  private static final String LINE_SEPARATOR = "line.separator";

  private static final String HSF_CONSUMER = "@HSFConsumer";

  private static final String BASE_PATH = System.getProperty("user.dir");

  private static final String FILE_SEPARATOR = File.separator;

  // 保存扫描到的所有java文件
  private ArrayList<File> fileList = new ArrayList<>();

  @Value("${hsf.consumer.packageName:com.alibaba.boot.hsf.annotation.HSFConsumer}")
  private String hsfConsumerPackageName;

  @Value("${spring.feignClient.packageName:org.springframework.cloud.openfeign.FeignClient}")
  private String feignClientPackageName;

  /**
   *   @param args
   *  args[0]：需要修改的项目目录
   */

  @Override
  public void run(String... args) {
    File fileCatalogue = new File(args[0]);
    File[] files = fileCatalogue.listFiles();
    if (files == null) {
      return;
    }
    filesAdd(files);
    replaceContent();
  }

  private void filesAdd(File[] files) {
    Arrays.stream(files).forEach(file -> {
      if (file.isFile() && file.getName().endsWith(".java")) {
        fileList.add(file);
      }
      if (file.isDirectory()) {
        // 在需要修改的项目resources文件下添加bootstrap.yml
        if ("resources".equals(file.getName())) {
          addYmlFile(new File(file.getAbsoluteFile() + FILE_SEPARATOR + "bootstrap.yml"));
        }
        filesAdd(file.listFiles());
      }
    });
  }

  private void addYmlFile(File file) {
    try {
      // 避免在windows或者linux系统中带来的差异性
      String originBootstrapContextPath =
          BASE_PATH + FILE_SEPARATOR + "src" + FILE_SEPARATOR + "main" + FILE_SEPARATOR + "resources";
      FileInputStream fileInputStream = new FileInputStream(
          originBootstrapContextPath + FILE_SEPARATOR + "bootstrap.txt");
      FileUtils.copyInputStreamToFile(fileInputStream, file);
    } catch (IOException ex) {
      // 如果添加配置文件失败，则后续进行手动添加
      LOGGER.error("add bootstrap.yml failed，need manual processing is required and message is {},", ex.getMessage());
    }
  }

  private void replaceContent() {
    fileList.forEach(file -> {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (int i = 0; i < lines.size(); i++) {
          String line = lines.get(i);
          // 处理import中的包名
          line = line.replace(hsfConsumerPackageName, feignClientPackageName);
          // 处理@HSFConsumer注解信息及接口信息
          if (line.contains(HSF_CONSUMER)) {
            String nextLine = lines.get(i + 1);
            String interfaceName = nextLine.trim().split(" ")[2].replace(";", "");
            String feignClientInfo = feignClientInfo(line, interfaceName, file);
            if (feignClientInfo != null) {
              line = line.replace(line.trim(), feignClientInfo);
              nextLine = nextLine.replace(nextLine.trim(), interfaceExtension(nextLine));
            }
            tempStream.write(line);
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            i++;
            tempStream.write(nextLine);
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            continue;
          }
          // 处理本文件中其余内容
          tempStream.write(line);
          tempStream.append(System.getProperty(LINE_SEPARATOR));
        }
        FileWriter fileWriter = new FileWriter(file);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception e) {
        LOGGER.error("file content replacement failed and message is {}", e.getMessage());
      }
    });
  }

  // FeignClient属性信息
  private String feignClientInfo(String line, String commonStr, File file) {
    StringBuilder stringBuilder = new StringBuilder();
    // 将目标字符串转化为JsonObject获取被调的微服务名称及进行属性信息拼接
    line = line.replace('=', ':');
    String jsonString = "{" + line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")) + "}";
    Object serviceGroupValue = JSONObject.parseObject(jsonString).get("serviceGroup");
    // 当开发者没有配置serviceGroup时，不抛出异常，保证后续内容正常修改，待内容修改全部完成后在error日志文件中查询相关不规范地方进行手动修改
    if (serviceGroupValue == null) {
      LOGGER.error("content replacement appear error, "
              + "need manual processing is required and message: Interface declaration missing serviceGroup and interfaceName is {} in file {} ",
          commonStr, file.getName());
      return null;
    }
    stringBuilder.append("@FeignClient(name = \"")
        .append(serviceGroupValue.toString())
        .append("\"")
        .append(", contextId = \"")
        .append(commonStr)
        .append("\", ")
        .append("path = \"/")
        .append(commonStr)
        .append("\")");
    return new String(stringBuilder);
  }

  // 接口拓展信息
  private String interfaceExtension(String line) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] s = line.trim().split(" ");
    stringBuilder.append("public interface ")
        .append(s[1])
        .append("Ext extends ")
        .append(s[1])
        .append("{}");
    return new String(stringBuilder);
  }
}
