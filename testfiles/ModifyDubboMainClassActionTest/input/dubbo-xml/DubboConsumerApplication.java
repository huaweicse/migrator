package com.huaweicloud.sample;

import org.springframework.context.annotation.ImportResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportResource("consumer.xml")
public class DubboConsumerApplication {
  public static void main(String[] args) {
    SpringApplication.run(DubboConsumerApplication.class, args);
  }
}