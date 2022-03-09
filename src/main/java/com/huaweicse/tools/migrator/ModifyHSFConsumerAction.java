package com.huaweicse.tools.migrator;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private ArrayList<File> fileList = new ArrayList<>();

  private static final String LINE_SEPARATOR = "line.separator";

  private static final String HSF_CONSUMER = "@HSFConsumer";

  /**
   *  考虑到不同版本依赖可能相关需要替换的class类型（HSFConsumer、FeignClient）所属包名不一致，
   *  所以采用参数模式进行数值传递，故我们对参数做如下规定：
   *
   *   @param args
   *  args[0]：需要修改的项目目录
   *  args[1]: HSFConsumer的完整包名
   *  args[2]: FeignClient的完整包名
   */

  @Override
  public void run(String... args) {
    File fileCatalogue = new File(args[0]);
    File[] files = fileCatalogue.listFiles();
    if (files == null) {
      return;
    }
    filesAdd(files);
    replaceContent(args);
  }

  private void filesAdd(File[] files) {
    Arrays.stream(files).forEach(file -> {
      if (file.isFile() && file.getName().endsWith(".java")) {
        fileList.add(file);
      }
      if (file.isDirectory()) {
        filesAdd(file.listFiles());
      }
    });
  }

  private void replaceContent(String... args) {
    fileList.forEach(file -> {
      try {
        File targetFile = new File(file.getAbsolutePath());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(targetFile));
        CharArrayWriter tempStream = new CharArrayWriter();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
          // 处理import中的包名
          line = line.replace(args[1], args[2]);
          // 处理@HSFConsumer注解信息及接口信息
          if (line.contains(HSF_CONSUMER)) {
            String nextLine = bufferedReader.readLine().replace(";", "");
            String commonStr = nextLine.trim().split(" ")[2];
            line = line.replace(line.trim(), feignClientInfo(line, commonStr, file));
            tempStream.write(line);
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            nextLine = nextLine.replace(nextLine.trim(), interfaceExtension(nextLine));
            tempStream.write(nextLine);
            tempStream.append(System.getProperty(LINE_SEPARATOR));
            continue;
          }
          // 处理其余文件内容
          tempStream.write(line);
          tempStream.append(System.getProperty(LINE_SEPARATOR));
        }
        bufferedReader.close();
        FileWriter fileWriter = new FileWriter(targetFile);
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
      serviceGroupValue = "missServiceGroup";
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
