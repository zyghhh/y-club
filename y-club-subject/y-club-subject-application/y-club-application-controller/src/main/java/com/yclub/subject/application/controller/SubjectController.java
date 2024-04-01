package com.yclub.subject.application.controller;

import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-03-29  16:49
 */
@RestController
public class SubjectController {
    @Resource
    private SubjectCategoryService subjectCategoryService;
    @RequestMapping("/hello")
    public  String test(){
        return "hello!";
    }

    @RequestMapping("/testDB")
    public  String testDB(){
        SubjectCategory subjectCategory = subjectCategoryService.queryById(1l);
        return subjectCategory.getCategoryName();
    }
}
