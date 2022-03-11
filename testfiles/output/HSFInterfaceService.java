
package com.huaweicse.tools.migrator;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;

public interface HSFInterfaceService {

  @ResponseBody
  @PostMapping(value = "/hello")
  String hello(String String);

  // 单行注释
  @ResponseBody
  @PostMapping(value = "/slComment")
  List<String> slComment(List list);

  /**
   *
   * @param map
   * @return
   */
  @ResponseBody
  @PostMapping(value = "/muComment")
  Map<String> muComment(Map map);
}
