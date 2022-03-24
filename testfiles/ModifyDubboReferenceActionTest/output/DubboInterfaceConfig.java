package com.huaweicse.config;

import com.huaweicse.test.api.HelloService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboInterfaceConfig {

	@FeignClient(name = "dubbo-provider", contextId = "helloService", path = "/helloService")
	public interface HelloServiceExt extends HelloService{}

}
