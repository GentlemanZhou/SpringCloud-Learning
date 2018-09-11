package com.mz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.zhou
 */
@RestController
public class HelloController {

    @RequestMapping("hi")
    public String sayHi() {
        return "Hi Spring Cloud";
    }
}
