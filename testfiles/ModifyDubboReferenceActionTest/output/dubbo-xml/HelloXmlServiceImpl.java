package com.huaweicse.test;

import com.huaweicse.test.api.HelloXmlService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/helloXmlService")
public class HelloXmlServiceImpl implements HelloXmlService {
    public String sayHello() {
        return "hello, the demo is for dubbo-xml.";
    }
}
