package com.yclub.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-15  10:58
 */
@SpringBootApplication
@ComponentScan("com.yclub")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class);
    }

}