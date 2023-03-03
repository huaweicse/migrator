package com.huaweicse.tools.migrator.hsf;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.huaweicse.tools.migrator.common.FileAction;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含com.alibaba.schedulerx.worker.processor.JavaProcessor 标签，
 *   如果存在，将其替换为 @XxlJob。
 */
@Component
public class ModifySchedulerJobAction extends FileAction {
  private static final String SCHEDULER_JOB_CLASS = "com.alibaba.schedulerx.worker.processor.JavaProcessor";

  private static final Pattern CLASS_DEF = Pattern.compile(
      "class [a-zA-Z]+[a-zA-Z0-9]* extends JavaProcessor");

  private static final Pattern METHOD_DEF = Pattern.compile(
      "ProcessResult process\\(JobContext [a-zA-Z0-9]+\\)");

  @Override
  public void run(String... args) throws Exception {
    List<File> acceptedFiles = acceptedFiles(args[0]);
    for (File file : acceptedFiles) {
      List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
      boolean notesBegin = false;
      int lineNumber = 0;
      String className = null;
      CharArrayWriter tempStream = new CharArrayWriter();
      for (; lineNumber < lines.size(); lineNumber++) {
        String line = lines.get(lineNumber);
        // 空行
        if (line.trim().isEmpty()) {
          writeLine(tempStream, line);
          continue;
        }
        // 行注释
        if (line.trim().startsWith("//")) {
          writeLine(tempStream, line);
          continue;
        }
        // 文本注释
        if (line.trim().startsWith("*/")) {
          notesBegin = false;
          writeLine(tempStream, line);
          continue;
        }
        if (notesBegin) {
          writeLine(tempStream, line);
          continue;
        }
        if (line.trim().startsWith("/**")) {
          notesBegin = true;
          writeLine(tempStream, line);
          continue;
        }

        // add import
        if (line.startsWith("package ")) {
          writeLine(tempStream, line);
          writeLine(tempStream, "");
          writeLine(tempStream, "import com.xxl.job.core.biz.model.ReturnT;");
          writeLine(tempStream, "import com.xxl.job.core.handler.annotation.XxlJob;");
          continue;
        }

        // import
        if (line.startsWith("import com.alibaba.schedulerx")) {
          continue;
        }

        // 类定义
        Matcher classMatcher = CLASS_DEF.matcher(line);
        if (classMatcher.find()) {
          String classDef = classMatcher.group();
          className = classDef.substring(classDef.indexOf("class ") + 6, classDef.indexOf("extends JavaProcessor") - 1);
          writeLine(tempStream, line.replace("extends JavaProcessor", ""));
          continue;
        }

        // 重载方法
        if ("@Override".equals(line.trim())) {
          String nextLine = lines.get(lineNumber + 1);
          Matcher methodMatcher = METHOD_DEF.matcher(nextLine);
          if (methodMatcher.find()) {
            if (className == null) {
              throw new IllegalStateException("wrong job file." + file.getAbsolutePath());
            }
            writeLine(tempStream, "    @XxlJob(\"" + className + "\")");
            String methodSign = methodMatcher.group();
            writeLine(tempStream, nextLine.replace(methodSign,
                "ReturnT<String> doJob(String param)"));
            lineNumber++;
            continue;
          }
        }

        if (line.contains("return new ProcessResult(true);")) {
          writeLine(tempStream, line.replace("return new ProcessResult(true);", "return ReturnT.SUCCESS;"));
          continue;
        }

        if (line.contains("ProcessResult pr = new ProcessResult(true);")) {
          writeLine(tempStream, line.replace("ProcessResult pr = new ProcessResult(true);",
              "ReturnT pr = ReturnT.SUCCESS;"));
          continue;
        }

        writeLine(tempStream, line);
      }

      OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
      tempStream.writeTo(fileWriter);
      fileWriter.close();
    }
  }

  private boolean isClassDef(String line) {
    return CLASS_DEF.matcher(line).matches();
  }

  @Override
  protected boolean isAcceptedFile(File file) throws IOException {
    if (!file.getName().endsWith(".java")) {
      return false;
    }
    return fileContains(file, SCHEDULER_JOB_CLASS);
  }
}
