

package com.huaweicse.tools.migrator;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFConsumer {

  @FeignClient(name = "consumer", contextId = "consumerService", path = "/consumerService")
  public interface ConsumerServiceExt extends ConsumerService{}

}
