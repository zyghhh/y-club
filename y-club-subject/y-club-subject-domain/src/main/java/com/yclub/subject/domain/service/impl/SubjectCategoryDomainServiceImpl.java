package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.domain.convert.SubjectCategoryConverter;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.service.SubjectCategoryDomainService;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:17
 */
@Service
@Slf4j
public class SubjectCategoryDomainServiceImpl implements SubjectCategoryDomainService {

    @Resource
    SubjectCategoryService subjectCategoryService;

    @Override
    public void add(SubjectCategoryBO subjectCategoryBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryController.add.bo:{}", JSON.toJSONString(subjectCategoryBO));
        }
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBOToCategory(subjectCategoryBO);
        subjectCategoryService.insert(subjectCategory);

    }
}