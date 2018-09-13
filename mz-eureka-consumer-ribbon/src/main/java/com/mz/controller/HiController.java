package com.mz.controller;

import com.mz.feign.FeignClientDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mr.zhou
 */
@RestController
public class HiController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FeignClientDemo feignClientDemo;
    @GetMapping("/sayHi")
    public String sayHi() {
        return restTemplate.getForObject("http://mz-eureka-client-one/hi", String.class);
    }

    @GetMapping("/hello")
    public String hello() {
        return feignClientDemo.sayHi();
    }

}
