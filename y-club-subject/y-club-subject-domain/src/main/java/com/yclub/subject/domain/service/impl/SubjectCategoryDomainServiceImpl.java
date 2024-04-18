package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.common.enums.IsDeletedFlagEnum;
import com.yclub.subject.domain.convert.SubjectCategoryConverter;
import com.yclub.subject.domain.convert.SubjectLabelConverter;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.entity.SubjectLabelBO;
import com.yclub.subject.domain.service.SubjectCategoryDomainService;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.entity.SubjectLabel;
import com.yclub.subject.infra.basic.entity.SubjectMapping;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import com.yclub.subject.infra.basic.service.SubjectLabelService;
import com.yclub.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    SubjectMappingService subjectMappingService;

    @Resource
    SubjectLabelService subjectLabelService;

    @Override
    public void add(SubjectCategoryBO subjectCategoryBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryController.add.bo:{}", JSON.toJSONString(subjectCategoryBO));
        }
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBOToCategory(subjectCategoryBO);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        subjectCategoryService.insert(subjectCategory);

    }

    @Override
    public Boolean update(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBOToCategory(subjectCategoryBO);
        int count = subjectCategoryService.update(subjectCategory);
        return count > 0;
    }

    @Override
    public Boolean delete(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBOToCategory(subjectCategoryBO);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        int count = subjectCategoryService.update(subjectCategory);
        return count > 0;
    }

    @Override
    public List<SubjectCategoryBO> queryCategory(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBOToCategory(subjectCategoryBO);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        List<SubjectCategory> subjectCategoryList = subjectCategoryService.queryCategory(subjectCategory);
        List<SubjectCategoryBO> boList = SubjectCategoryConverter.INSTANCE
                .convertToBOList(subjectCategoryList);
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryController.queryPrimaryCategory.boList:{}",
                    JSON.toJSONString(boList));
        }
        boList.forEach(bo -> {
            Integer subjectCount = subjectCategoryService.querySubjectCount(bo.getId());
            bo.setCount(subjectCount);
        });
        return boList;
    }

    @Override
    public List<SubjectCategoryBO> queryCategoryAndLabel(SubjectCategoryBO subjectCategoryBO) {
        SubjectCategory subjectCategory = SubjectCategoryConverter.INSTANCE
                .convertBOToCategory(subjectCategoryBO);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        List<SubjectCategory> subjectCategoryList = subjectCategoryService.queryCategory(subjectCategory);
        List<SubjectCategoryBO> boList = SubjectCategoryConverter.INSTANCE.convertToBOList(subjectCategoryList);

        //再拿到每个 子类的所有标签
        boList.forEach(subjectCategory1 -> {
            SubjectMapping subjectMapping = new SubjectMapping();
            subjectMapping.setCategoryId(subjectCategory1.getId());
           List<SubjectMapping> subjectMappingList =  subjectMappingService.queryByCondition(subjectMapping);
            List<Long> labelIds = subjectMappingList.stream()
                    .map(SubjectMapping::getLabelId).collect(Collectors.toList());
            List<SubjectLabel> subjectLabelList = subjectLabelService.batchQueryByIds(labelIds);
            List<SubjectLabelBO> subjectLabelBOS = SubjectLabelConverter.INSTANCE.convertToBOList(subjectLabelList);
            subjectCategory1.setLabelBOList(subjectLabelBOS);
        });
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryController.queryPrimaryCategory.boList:{}",
                    JSON.toJSONString(boList));
        }
        return boList;
    }
}