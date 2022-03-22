package com.huaweicloud.sample.api;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "dubbo-provider", contextId = "helloService", path = "/helloService")
public interface HelloService {
  @ResponseBody
  @PostMapping(value = "/hello")
  String hello(@RequestParam(value="name") String name);
}
