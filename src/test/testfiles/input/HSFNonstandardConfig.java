

package com.huaweicse.tools.migrator;

import com.alibaba.boot.hsf.annotation.HSFConsumer;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFNonstandardConfig {

  @HSFConsumer(clientTimeout = 3000, serviceVersion = "1.0.0")
  private AbnomalService abnomalService;

  @HSFConsumer(clientTimeout = 3000, serviceGroup = "nomal", serviceVersion = "1.0.0")
  private NomalService nomalService;
}
