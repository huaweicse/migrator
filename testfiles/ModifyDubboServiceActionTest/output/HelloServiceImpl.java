package com.huaweicloud.sample;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huaweicloud.sample.api.HelloService;

@RestController
@RequestMapping("/helloService")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name + ", I am from provider";
    }
}
