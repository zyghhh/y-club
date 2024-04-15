package com.yclub.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-15  9:25
 */
@SpringBootApplication
@ComponentScan("com.yclub")
//@MapperScan("com.yclub.**.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class);
    }

}