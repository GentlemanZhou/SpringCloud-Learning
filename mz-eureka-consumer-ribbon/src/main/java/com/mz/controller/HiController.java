package com.mz.controller;

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

    @GetMapping("/sayHi")
    public String dc() {
        return restTemplate.getForObject("http://mz-eureka-client-one/hi", String.class);
    }
}
