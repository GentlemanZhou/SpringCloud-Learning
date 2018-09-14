package com.mz.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Mr.zhou
 */
@Component
@FeignClient(value = "mz-eureka-client-one")
public interface FeignClientDemo {
    @GetMapping("/hi")
    String sayHi();

    @PostMapping("/hi1")
    String sayHi(@RequestBody String aaa);
}
