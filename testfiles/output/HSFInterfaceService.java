
package com.huaweicse.tools.migrator;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

public interface HSFInterfaceService {

    @ResponseBody
    @PostMapping(value = "/hello", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  String hello(@RequestHeader(value="str") String str);

  // 无参数
    @ResponseBody
    @PostMapping(value = "/noArg", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  List<String> noArg();

    @ResponseBody
    @PostMapping(value = "/str", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  List<String> str(@RequestHeader(value="string") String string);

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
    @PostMapping(value = "/mix", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  String mix(@RequestBody List<String> stringList, @RequestParam(value="num") Integer num);

    @ResponseBody
    @PostMapping(value = "/single", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  ResultBody single(@RequestBody EntityBody entityBody, @RequestParam(value="count") Long count, @RequestParam(value="num") Double num);

    @ResponseBody
    @PostMapping(value = "/generics", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  ResultBody generics(@RequestBody Map<String, Object> entityBody, @RequestParam(value="count") Long count, @RequestParam(value="num") Double num);

    @ResponseBody
    @PostMapping(value = "/queryWithoutOrm", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  List<Map<String, Object>> queryWithoutOrm(@RequestHeader(value="token") String token, @RequestHeader(value="sql") String sql, @RequestBody Object... args);

  // 该情况下打印error日志，稍后手动进行参数重构
    @ResponseBody
    @PostMapping(value = "/manyBody", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  ResultBody manyBody(@RequestBody BodyOne bodyOne, @RequestBody BodyTwo bodyTwo, @RequestParam(value="count") Long count, @RequestParam(value="num") Double num);

    @ResponseBody
    @PostMapping(value = "/importBatchData", produces = "x-application/hessian2", consumes = "x-application/hessian2")
  ServiceResult importBatchData(@RequestHeader(value="token") String token, @RequestHeader(value="orderId") String orderId, @RequestBody Map<String, List<String[]>> dataMap);
}
