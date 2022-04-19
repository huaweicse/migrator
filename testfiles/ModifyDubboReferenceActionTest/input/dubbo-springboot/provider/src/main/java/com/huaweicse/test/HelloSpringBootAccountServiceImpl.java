package com.huaweicse.test;

import org.apache.dubbo.config.annotation.DubboService;

import com.huaweicse.test.api.HelloSpringBootAccountService;

@DubboService
public class HelloSpringBootAccountServiceImpl implements HelloSpringBootAccountService {
  @Override
  public String accountInfo(String name) {
    return "account info";
  }
}
