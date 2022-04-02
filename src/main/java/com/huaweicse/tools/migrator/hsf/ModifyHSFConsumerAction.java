package com.huaweicse.tools.migrator.hsf;

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
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.huaweicse.tools.migrator.common.Const;
import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFConsumer 标签，如果存在，将其替换为 @FeignClient。
 *   替换过程中，会替换 import，一并修改 import。
 */
@Component
public class ModifyHSFConsumerAction extends FileAction {

  private static final Logger LOGGER = LoggerFactory.getLogger(ModifyHSFConsumerAction.class);

  private static final String HSF_CONSUMER = "@HSFConsumer";

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, HSF_CONSUMER);
  }

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    replaceContent(acceptedFiles);
  }

  private void replaceContent(List<File> acceptedFiles) {
    acceptedFiles.forEach(file -> {
      try {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        CharArrayWriter tempStream = new CharArrayWriter();
        for (int i = 0; i < lines.size(); i++) {
          String line = lines.get(i);
          // 处理import中的包名
          line = line.replace(Const.HSF_CONSUMER_PACKAGE_NAME, Const.FEIGN_CLIENT_PACKAGE_NAME);
          // 处理@HSFConsumer注解信息及接口信息
          if (line.contains(HSF_CONSUMER)) {
            String nextLine = lines.get(i + 1);
            String[] interfaceLine = nextLine.trim().split(" ");
            if (interfaceLine.length < 3) {
              LOGGER.error(ERROR_MESSAGE,
                  "Interface definition not valid under @HSFConsumer annotation.",
                  file.getAbsolutePath(), i);

              writeLine(tempStream, line);
            } else {
              String interfaceName = interfaceLine[2].replace(";", "");
              String feignClientInfo = feignClientInfo(line, interfaceName);
              if (feignClientInfo != null) {
                line = line.replace(line.trim(), feignClientInfo);
                nextLine = nextLine.replace(nextLine.trim(), interfaceExtension(nextLine));
                writeLine(tempStream, line);
                writeLine(tempStream, nextLine);
                i++;
              } else {
                LOGGER.error(ERROR_MESSAGE,
                    "Interface declaration missing serviceGroup and interfaceName.",
                    file.getAbsolutePath(), i);
                writeLine(tempStream, line);
              }
            }
            continue;
          }
          writeLine(tempStream, line);
        }
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        tempStream.writeTo(fileWriter);
        fileWriter.close();
      } catch (Exception e) {
        LOGGER.error("Process file [{}] failed", file.getAbsolutePath(), e);
      }
    });
  }

  // FeignClient属性信息
  private String feignClientInfo(String line, String commonStr) {
    StringBuilder stringBuilder = new StringBuilder();
    // 将目标字符串转化为JsonObject获取被调的微服务名称及进行属性信息拼接
    line = line.replace('=', ':');
    String jsonString = "{" + line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")) + "}";
    Object serviceGroupValue = JSONObject.parseObject(jsonString).get("serviceGroup");
    // 当开发者没有配置serviceGroup时，不抛出异常，保证后续内容正常修改，待内容修改全部完成后在error日志文件中查询相关不规范地方进行手动修改
    if (serviceGroupValue == null) {
      return null;
    }
    stringBuilder.append("@FeignClient(name = \"")
        .append(serviceGroupValue)
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
