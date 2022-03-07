package com.huaweicse.tools.migrator;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，首先扫描出包含 @HSFProvider 标签的文件，并从中解析出需要处理的 JAVA interface文件，
 *   然后将 interface 文件修改为 REST 风格。 替换过程中，会替换 import，一并修改 import。
 */
public class ModifyHSFInterface2RestAction implements Action {
  @Override
  public void run(String... args) {
    // TODO
    throw new RuntimeException("not implemented");
  }
}
