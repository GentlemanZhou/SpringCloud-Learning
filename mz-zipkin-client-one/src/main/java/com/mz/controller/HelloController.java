package com.mz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mr.zhou
 */
@RestController
public class HelloController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    HelloFeign helloFeign;
    @RequestMapping("rest")
    public String sayHiByRest() {
        return restTemplate.getForEntity("http://mz-zipkin-client-two/hi", String.class).getBody();
    }

    @RequestMapping("feign")
    public String sayHiByFeign() {
        return helloFeign.sayHi();
    }

    @RequestMapping("fail")
    public String error() {
        return restTemplate.getForEntity("http://mz-zipkin-client-two/hi2", String.class).getBody();
    }

}

