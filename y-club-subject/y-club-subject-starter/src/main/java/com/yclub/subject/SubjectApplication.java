package com.yclub.subject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-03-29  16:12
 */
@SpringBootApplication
@ComponentScan("com.yclub")
public class SubjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubjectApplication.class);
    }

}
