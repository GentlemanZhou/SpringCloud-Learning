package com.mz.feign;

import org.springframework.stereotype.Component;

/**
 * @author Mr.zhou
 */
@Component
public class FeignClientDemoHystrix implements FeignClientDemo{

    @Override
    public String sayHi() {
        return "the service is down or has some error";
    }

    @Override
    public String sayHi(String aaa) {
        return "the service is down or has some error";
    }
}
