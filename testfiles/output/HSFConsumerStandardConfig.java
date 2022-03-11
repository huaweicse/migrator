

package com.huaweicse.tools.migrator;

import com.alibaba.boot.hsf.annotation.HSFConsumer;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFConsumerStandardConfig {

  @HSFConsumer(clientTimeout = 3000, serviceGroup = "travel", serviceVersion = "1.0.0")
  private TravelService travelService;

  @HSFConsumer(clientTimeout = 3000, serviceGroup = "finance", serviceVersion = "1.0.0")
  private FinanceService financeService;

  @HSFConsumer(clientTimeout = 3000, serviceGroup = "user", serviceVersion = "1.0.0")
  private UserService userService;
}
