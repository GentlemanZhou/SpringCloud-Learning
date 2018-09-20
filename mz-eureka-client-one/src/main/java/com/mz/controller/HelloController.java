package com.mz.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.zhou
 */
@RestController
@RefreshScope
public class HelloController {
    @Value("${server.port}")
    private String serverPort;
    @Value("${info.message:error}")
    private String infoMessage;
    @RequestMapping("hi")
    public String sayHi() {
        return "Hi Spring Cloud, running in port :" + serverPort + "     info.message is : " + infoMessage;
    }
}
