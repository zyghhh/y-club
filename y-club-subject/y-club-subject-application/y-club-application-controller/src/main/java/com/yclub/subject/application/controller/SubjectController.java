package com.yclub.subject.application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-03-29  16:49
 */
@RestController
public class SubjectController {
    @RequestMapping("/hello")
    public  String test(){
        return "hello!";
    }
}
