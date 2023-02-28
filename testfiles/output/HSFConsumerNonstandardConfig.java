

package com.huaweicse.tools.migrator;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFConsumerNonstandardConfig {

  @FeignClient(name = "${feign.client.HSFConsumerNonstandardConfig}", contextId = "missGroupService", path = "/missGroupService")
  public interface MissGroupServiceExt extends MissGroupService{}

  @FeignClient(name = "nomal", contextId = "nomalService", path = "/nomalService")
  public interface NomalServiceExt extends NomalService{}
}
