package com.huaweicse.config;

import com.huaweicse.test.api.HelloAnnotationService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboInterfaceConfig {

	@FeignClient(name = "dubbo-annotation-provider", contextId = "helloAnnotationService", path = "/helloAnnotationService")
	public interface HelloAnnotationServiceExt extends HelloAnnotationService{}

}
