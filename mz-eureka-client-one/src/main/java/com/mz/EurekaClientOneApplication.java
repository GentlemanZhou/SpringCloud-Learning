package com.mz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Mr.zhou
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientOneApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientOneApplication.class, args);
    }
}
