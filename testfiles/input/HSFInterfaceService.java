
package com.huaweicse.tools.migrator;

public interface HSFInterfaceService {

  String hello(String str);

  // 无参数
  List<String> noArg();

  List<String> str(String string);

  // 单行注释
  List<String> slComment(List<String> list);

  /**
   *
   * @param map
   * @return
   */
  Map<String> muComment(Map map);

  String mix(List<String> stringList, Integer num);

  ResultBody single(EntityBody entityBody, Long count, Double num);

  // 该情况下打印error日志，稍后手动进行参数重构
  ResultBody manyBody(BodyOne bodyOne, BodyTwo bodyTwo, Long count, Double num);
}