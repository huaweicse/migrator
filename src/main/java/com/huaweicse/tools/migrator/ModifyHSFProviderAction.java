package com.huaweicse.tools.migrator;

/**
 * 功能描述：
 *   扫描目录下面的所有JAVA文件，识别文件是否包含 @HSFProvider 标签，如果存在，将其替换为 @RestController。
 *   替换过程中，会替换 import，一并修改 import。
 */
public class ModifyHSFProviderAction implements Action {
  @Override
  public void run(String... args) {
    // TODO
    throw new RuntimeException("not implemented");
  }
}
