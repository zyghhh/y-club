package com.yclub.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.infra.basic.entity.SubjectInfoEs;
import com.yclub.subject.infra.basic.entity.UserInfo;
import com.yclub.subject.infra.basic.rpc.UserRpc;
import com.yclub.subject.infra.basic.service.SubjectEsService;
import com.yclub.subject.infra.basic.service.SubjectEsServiceByData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Resource
    private SubjectEsServiceByData subjectEsServiceByData;


    @GetMapping("testFeign")
    public void testFeign(){
        UserInfo userInfo = userRpc.getUserInfo("qqqqq");
        log.info("testFeign:{}",userInfo);
    }

    @GetMapping("createIndex")
    public void createIndex(){
        subjectEsServiceByData.createIndex();
    }
    @GetMapping("addDocs")
    public void addDocs(){
        subjectEsServiceByData.addDocs();
    }
    @GetMapping("search")
    public void search(){
        subjectEsServiceByData.search();
    }
    @GetMapping("find")
    public void find(){
        subjectEsServiceByData.find();
    }

    @PostMapping("querySubjectByKeyWord")
    public void querySubjectByKeyWord(){
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setKeyWord("Redis");
        PageResult<SubjectInfoEs> pageResult = subjectEsService.querySubjectList(subjectInfoEs);
        log.info("querySubjectByKeyWord:{}", JSON.toJSONString(pageResult));
    }

}
