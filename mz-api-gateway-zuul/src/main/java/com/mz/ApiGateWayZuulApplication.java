package com.mz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Mr.zhou
 */
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ApiGateWayZuulApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ApiGateWayZuulApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApiGateWayZuulApplication.class);
    }
}
