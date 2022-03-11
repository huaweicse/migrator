
package com.huaweicse.tools.migrator;

public interface HSFInterfaceService {

  String hello(String String);

  // 单行注释
  Map<String> slComment(List list);

  /**
   *
   * @param map
   * @return
   */
  Map<String> muComment(Map map);
}