package com.huaweicloud.sample;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DubboConsumerApplication {
  public static void main(String[] args) {
    SpringApplication.run(DubboConsumerApplication.class, args);
  }
}
