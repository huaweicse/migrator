package com.huaweicse.test;

import org.apache.dubbo.config.annotation.DubboService;

import com.huaweicse.test.api.HelloSpringBootService;

@DubboService
public class HelloSpringBootServiceImpl implements HelloSpringBootService {
  @Override
  public String hello(String name) {
    return "Hello " + name + ", I am from provider";
  }
}