

package com.huaweicse.tools.migrator;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFConsumerNonstandardConfig {

  @HSFConsumer(clientTimeout = 3000, serviceVersion = "1.0.0")
  private MissGroupService missGroupService;

  @FeignClient(name = "nomal", contextId = "nomalService", path = "/nomalService")
  public interface NomalServiceExt extends NomalService{}
}
