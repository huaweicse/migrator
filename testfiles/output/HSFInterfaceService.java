
package com.huaweicse.tools.migrator;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

public interface HSFInterfaceService {

  @ResponseBody
  @PostMapping(value = "/hello")
  String hello(@RequestParam(value="str") String str);

  // 无参数
  @ResponseBody
  @PostMapping(value = "/noArg", produces = "x-application/hessian2")
  List<String> noArg();

  @ResponseBody
  @PostMapping(value = "/str", produces = "x-application/hessian2")
  List<String> str(@RequestParam(value="string") String string);

  // 单行注释
  @ResponseBody
  @PostMapping(value = "/slComment", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  List<String> slComment(@RequestBody List<String> list);

  /**
   *
   * @param map
   * @return
   */
  @ResponseBody
  @PostMapping(value = "/muComment", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  Map<String> muComment(@RequestBody Map map);

  @ResponseBody
  @PostMapping(value = "/mix", consumes = "x-application/hessian2")
  String mix(@RequestBody List<String> stringList, @RequestParam(value="num") Integer num);

  @ResponseBody
  @PostMapping(value = "/single", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  ResultBody single(@RequestBody EntityBody entityBody, @RequestParam(value="count") Long count, @RequestParam(value="num") Double num);

  // 该情况下打印error日志，稍后手动进行参数重构
  @ResponseBody
  @PostMapping(value = "/manyBody", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  ResultBody manyBody(BodyOne bodyOne, BodyTwo bodyTwo, Long count, Double num);
}
