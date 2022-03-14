
package com.huaweicse.tools.migrator;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

public interface HSFInterfaceService {

  // 无参数
  @ResponseBody
  @PostMapping(value = "/noArg", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  List<String> noArg();

  @ResponseBody
  @PostMapping(value = "/str", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  List<String> str(@RequestBody String string);

  // 单行注释
  @ResponseBody
  @PostMapping(value = "/slComment", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  List<String> slComment(@RequestBody List<String> list);

  /**
   *
   * @param map
   * @return
   */
  @ResponseBody
  @PostMapping(value = "/muComment", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  Map<String> muComment(@RequestBody Map map);

  @ResponseBody
  @PostMapping(value = "/mix", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  String mix(@RequestBody List<String> stringList, @RequestParam Integer num);

  @ResponseBody
  @PostMapping(value = "/single", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  ResultBody single(@RequestBody EntityBody entityBody, @RequestParam Long count, @RequestParam Double num);

  // 该情况下打印error日志，稍后手动进行参数重构
  @ResponseBody
  @PostMapping(value = "/manyBody", consumes = "x-application/hessian2", produces = "x-application/hessian2")
  ResultBody manyBody(BodyOne bodyOne, BodyTwo bodyTwo, Long count, Double num);
}
