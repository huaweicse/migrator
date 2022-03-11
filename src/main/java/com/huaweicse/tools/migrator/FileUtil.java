package com.huaweicse.tools.migrator;

import java.io.*;

public class FileUtil {
  /**
   * 复制文件夹
   *
   * @param resource 源路径
   * @param target   目标路径
   */
  public static void copyFolder(String resource, String target) throws Exception {

    File resourceFile = new File(resource);
    if (!resourceFile.exists()) {
      throw new Exception("源目标路径：[" + resource + "] 不存在...");
    }
    File targetFile = new File(target);
    if (!targetFile.exists()) {
      targetFile.mkdirs();
    }

    // 获取源文件夹下的文件夹或文件
    File[] resourceFiles = resourceFile.listFiles();

    for (File file : resourceFiles) {

      File file1 = new File(targetFile.getAbsolutePath() + File.separator + file.getName());
      // 复制文件
      if (file.isFile()) {
        System.out.println("文件" + file.getName());
        copyFile(file, file1);
      }
      // 复制文件夹
      if (file.isDirectory()) {// 复制源文件夹
        String dir1 = file.getAbsolutePath();
        // 目的文件夹
        String dir2 = file1.getAbsolutePath();
        copyFolder(dir1, dir2);
      }
    }
  }

  /**
   * 复制文件
   *
   * @param resource
   * @param target
   */
  public static void copyFile(File resource, File target) throws Exception {
    // 输入流 --> 从一个目标读取数据
    // 输出流 --> 向一个目标写入数据

    long start = System.currentTimeMillis();

    // 文件输入流并进行缓冲
    FileInputStream inputStream = new FileInputStream(resource);
    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

    // 文件输出流并进行缓冲
    FileOutputStream outputStream = new FileOutputStream(target);
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

    // 缓冲数组
    // 大文件 可将 1024 * 2 改大一些，但是 并不是越大就越快
    byte[] bytes = new byte[1024 * 2];
    int len = 0;
    while ((len = inputStream.read(bytes)) != -1) {
      bufferedOutputStream.write(bytes, 0, len);
    }
    // 刷新输出缓冲流
    bufferedOutputStream.flush();
    //关闭流
    bufferedInputStream.close();
    bufferedOutputStream.close();
    inputStream.close();
    outputStream.close();
    long end = System.currentTimeMillis();
    System.out.println("耗时：" + (end - start) / 1000 + " s");
  }
}

