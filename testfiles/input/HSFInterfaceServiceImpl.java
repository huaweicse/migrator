
package com.huaweicse.tools.migrator;

import com.alibaba.boot.hsf.annotation.HSFProvider;


@HSFProvider(serviceInterface = HSFInterfaceService.class, serviceVersion = "1.0.0")
public class HSFInterfaceServiceImpl implements HSFInterfaceService {

  @Override
  public String hello(String name) {
    return name;
  }

}