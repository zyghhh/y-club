package com.yclub.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yclub.subject.application.convert.SubjectCategoryDTOConverter;
import com.yclub.subject.application.dto.SubjectCategoryDTO;
import com.yclub.subject.common.entity.Result;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.service.SubjectCategoryDomainService;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-03-29  16:49
 */
@RestController
@RequestMapping("/subject/category")
@Slf4j
public class SubjectController {
    @Resource
    private SubjectCategoryDomainService subjectCategoryDomainService;

    @Resource
    private SubjectCategoryService subjectCategoryService;
    @RequestMapping("/add")
    public Result<Boolean> test(@RequestBody SubjectCategoryDTO subjectCategoryDTO){
        try{
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.add.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getParentId(),"parentId is null");
            Preconditions.checkNotNull(subjectCategoryDTO.getCategoryType(),"CategoryType is null");
            Preconditions.checkArgument(!StringUtils.isEmpty(subjectCategoryDTO.getCategoryName()),"CategoryName is null");
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE
                    .convertDTOToBOCategory(subjectCategoryDTO);
            subjectCategoryDomainService.add(subjectCategoryBO);
            return Result.ok(true);
        } catch (Exception e){
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }


    @RequestMapping("/testDB")
    public  String testDB(){
        SubjectCategory subjectCategory = subjectCategoryService.queryById(1l);
        return subjectCategory.getCategoryName();
    }


}
