
package com.huaweicse.tools.migrator;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

public interface HSFInterfaceService {

  // 无参数
  @ResponseBody
  @PostMapping(value = "/noArg", consumes = "hessian", produces = "hessian")
  List<String> noArg();

  // 单行注释
  @ResponseBody
  @PostMapping(value = "/slComment", consumes = "hessian", produces = "hessian")
  List<String> slComment(@RequestBody List<String> list);

  /**
   *
   * @param map
   * @return
   */
  @ResponseBody
  @PostMapping(value = "/muComment", consumes = "hessian", produces = "hessian")
  Map<String> muComment(@RequestBody Map map);

  @ResponseBody
  @PostMapping(value = "/mix", consumes = "hessian", produces = "hessian")
  String mix(@RequestParam String string, @RequestParam Integer num, @RequestParam List<String> stringList);

  @ResponseBody
  @PostMapping(value = "/single", consumes = "hessian", produces = "hessian")
  ResultBody single(@RequestBody EntityBody entityBody, @RequestParam Long count, @RequestParam Double num);

  // 该情况下打印error日志，稍后手动进行参数重构
  @ResponseBody
  @PostMapping(value = "/manyBody", consumes = "hessian", produces = "hessian")
  ResultBody manyBody(@RequestBody BodyOne bodyOne, @RequestBody BodyTwo bodyTwo, @RequestParam Long count, @RequestParam Double num);
}
