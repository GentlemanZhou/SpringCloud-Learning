package com.mz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.zhou
 */
@RestController
public class HelloController {
    @Value("${server.port}")
    private String serverPort;
    @RequestMapping("hi")
    public String sayHi() {
        return "Hi Spring Cloud" + serverPort;
    }
}
