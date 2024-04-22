package com.yclub.subject.application.controller;

import com.yclub.subject.infra.basic.entity.UserInfo;
import com.yclub.subject.infra.basic.rpc.UserRpc;
import com.yclub.subject.infra.basic.service.SubjectEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-20  20:03
 */
@RestController
@RequestMapping("/subject/category")
@Slf4j
public class TestController {
    @Resource
    private UserRpc userRpc;

    @Resource
    private SubjectEsService subjectEsService;


    @GetMapping("testFeign")
    public void testFeign(){
        UserInfo userInfo = userRpc.getUserInfo("qqqqq");
        log.info("testFeign:{}",userInfo);
    }

    @GetMapping("createIndex")
    public void createIndex(){
        subjectEsService.createIndex();
    }


    @GetMapping("addDocs")
    public void addDocs(){
        subjectEsService.addDocs();
    }
    @GetMapping("search")
    public void search(){
        subjectEsService.search();
    }
    @GetMapping("find")
    public void find(){
        subjectEsService.find();
    }
}
