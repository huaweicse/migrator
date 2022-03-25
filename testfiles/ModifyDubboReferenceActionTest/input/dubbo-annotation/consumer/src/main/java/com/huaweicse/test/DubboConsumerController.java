package com.huaweicse.test;

import org.apache.dubbo.config.annotation.DubboReference;

import com.huaweicse.test.api.HelloService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboConsumerController {

  @DubboReference
  private HelloService helloService;

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello(@RequestParam("name") String name) {
    return helloService.hello(name);
  }
}