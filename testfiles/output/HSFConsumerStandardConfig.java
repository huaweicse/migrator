

package com.huaweicse.tools.migrator;

import org.springframework.cloud.openfeign.FeignClient;


import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFConsumerStandardConfig {

  @FeignClient(name = "travel", contextId = "travelService", path = "/travelService")
  public interface TravelServiceExt extends TravelService{}

  @FeignClient(name = "finance", contextId = "financeService", path = "/financeService")
  public interface FinanceServiceExt extends FinanceService{}

  @FeignClient(name = "user", contextId = "userService", path = "/userService")
  public interface UserServiceExt extends UserService{}
}
