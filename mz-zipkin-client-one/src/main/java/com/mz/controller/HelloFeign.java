package com.mz.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Mr.zhou
 */
@Component
@FeignClient(value="mz-zipkin-client-two")
public interface HelloFeign {
    @GetMapping("/hi")
    String sayHi();
}
