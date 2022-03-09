

package com.huaweicse.tools.migrator;

import com.alibaba.boot.hsf.annotation.HSFConsumer;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFConsumer {

  @HSFConsumer(clientTimeout = 3000, serviceGroup = "consumer", serviceVersion = "1.0.0")
  private ConsumerService consumerService;

}
