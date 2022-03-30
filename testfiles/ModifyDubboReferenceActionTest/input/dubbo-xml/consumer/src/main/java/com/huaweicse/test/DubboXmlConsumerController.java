package com.huaweicse.test;

import com.huaweicse.test.api.HelloXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DubboXmlConsumerController {

    @Autowired
    private HelloXmlService helloXmlService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return helloXmlService.sayHello();
    }
}
