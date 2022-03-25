package com.huaweicse.test;

import org.apache.dubbo.config.annotation.DubboService;

import com.huaweicse.test.api.HelloService;

@DubboService
public class HelloServiceImpl implements HelloService {
  @Override
  public String hello(String name) {
    return "Hello " + name + ", I am from provider";
  }
}