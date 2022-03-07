package com.huaweicse.tools.migrator;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述：
 * 扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFProvider 标签，如果存在，将其替换为 @RestController。
 * 替换过程中，会替换 import，一并修改 import。
 */
public class ModifyHSFProviderAction implements Action {
  public static String PROJECT_PATH = System.getProperty("user.dir");
  @Override
  public void run(String... args) {
    System.out.println(PROJECT_PATH);
    Path start = FileSystems.getDefault().getPath(PROJECT_PATH);
    try {
      List<Path> list = Files.walk(start).filter(childpath -> childpath.toFile().isFile()).filter(path -> path.toString().endsWith(".java")).filter(path -> ifHasAnnotion(path)).collect(Collectors.toList());
      list.forEach(path -> replace(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean ifHasAnnotion(Path path) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(path.toFile()));
      String tempString = null;
      int line = 1;
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        if (tempString.contains("@ssddasd")) {
          reader.close();
          return true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }
    return false;
  }

  public void replace(Path path) {
    System.out.println(path.toString());
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(path.toFile()));
      PrintWriter pw = null;
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path.toFile(), true)));
      String tempString = null;
      StringBuffer buff = new StringBuffer();
      String separator = System.getProperty("line.separator");//平台换行!
      int line = 1;
      // 一次读入一行，直到读入null为文件结束
      while ((tempString = reader.readLine()) != null) {
        if (tempString.contains("@ssddasd")) {
          // 显示行号
          System.out.println("line " + line + ": " + tempString + path.toString());
          tempString = new String(
                  tempString.replace("@ssddasd", "@ssddasd"));
        }
        buff.append(tempString + separator);
        line++;
      }
      reader.close();
      pw = new PrintWriter(new FileWriter(path.toFile()), true);
      pw.println(buff);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
        }
      }
    }

  }

  public static void main(String[] args) {
    new ModifyHSFProviderAction().run();
  }
}



