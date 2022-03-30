package com.huaweicse.test;

import com.huaweicse.test.api.HelloXmlService;

public class HelloXmlServiceImpl implements HelloXmlService {
    public String sayHello() {
        return "hello, the demo is for dubbo-xml.";
    }
}
