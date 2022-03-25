package com.huaweicse.test;

import org.apache.dubbo.config.annotation.DubboService;

import com.huaweicse.test.api.HelloAnnotationService;

@DubboService
public class HelloAnnotationServiceImpl implements HelloAnnotationService {
  @Override
  public String hello(String name) {
    return "Hello " + name + ", I am from provider";
  }
}