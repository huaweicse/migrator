package com.huaweicse.config;

import com.huaweicse.test.api.HelloXmlService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboInterfaceConfig {

	@FeignClient(name = "dubbo-xml-provider", contextId = "helloXmlService", path = "/helloXmlService")
	public interface HelloXmlServiceExt extends HelloXmlService{}

}
