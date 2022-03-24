package com.huaweicse.test.api;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

public interface HelloService {
  @ResponseBody
  @PostMapping(value = "/hello")
  String hello(@RequestParam(value="name") String name);
}
