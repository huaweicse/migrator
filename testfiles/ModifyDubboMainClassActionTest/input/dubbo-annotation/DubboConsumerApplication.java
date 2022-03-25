package com.huaweicloud.sample;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDubbo
@PropertySource("classpath:/spring/dubbo-consumer.properties")
public class DubboConsumerApplication {
  public static void main(String[] args) {
    SpringApplication.run(DubboConsumerApplication.class, args);
  }
}