package com.huaweicse.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.huaweicse.test.api.HelloSpringBootService;
import com.huaweicse.test.api.HelloSpringBootAccountService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboSpringBootConsumerController {

  @Autowired
  private HelloSpringBootService helloSpringBootService;

  @Autowired
  HelloSpringBootAccountService helloSpringBootAccountService;

  @RequestMapping(value = "/hello", method = RequestMethod.GET)
  public String hello(@RequestParam("name") String name) {
    return helloSpringBootService.hello(name);
  }

  @RequestMapping(value = "/accountInfo", method = RequestMethod.GET)
  public String accountInfo(@RequestParam("name") String name) {
    return helloSpringBootAccountService.accountInfo(name);
  }
}
