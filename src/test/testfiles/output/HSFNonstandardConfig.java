

package com.huaweicse.tools.migrator;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.context.annotation.Configuration;


@Configuration
public class HSFNonstandardConfig {

  @FeignClient(name = "missServiceGroup", contextId = "abnomalService", path = "/abnomalService")
  public interface AbnomalServiceExt extends AbnomalService{}

  @FeignClient(name = "nomal", contextId = "nomalService", path = "/nomalService")
  public interface NomalServiceExt extends NomalService{}
}
