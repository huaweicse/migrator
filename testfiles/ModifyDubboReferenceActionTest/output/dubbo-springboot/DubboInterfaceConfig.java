package com.huaweicse.config;

import com.huaweicse.test.api.HelloSpringBootService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboInterfaceConfig {

	@FeignClient(name = "dubbo-springboot-provider", contextId = "helloSpringBootService", path = "/helloSpringBootService")
	public interface HelloSpringBootServiceExt extends HelloSpringBootService{}

}
