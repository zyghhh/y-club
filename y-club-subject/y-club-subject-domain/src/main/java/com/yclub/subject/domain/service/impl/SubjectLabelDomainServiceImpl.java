package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.common.enums.CategoryTypeEnum;
import com.yclub.subject.common.enums.IsDeletedFlagEnum;
import com.yclub.subject.domain.convert.SubjectLabelConverter;
import com.yclub.subject.domain.entity.SubjectLabelBO;
import com.yclub.subject.domain.service.SubjectLabelDomainService;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.entity.SubjectLabel;
import com.yclub.subject.infra.basic.entity.SubjectMapping;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import com.yclub.subject.infra.basic.service.SubjectLabelService;
import com.yclub.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-02  15:24
 */
@Service
@Slf4j
public class SubjectLabelDomainServiceImpl implements SubjectLabelDomainService {
    @Resource
    private SubjectLabelService subjectLabelService;

    @Resource
    private SubjectMappingService subjectMappingService;

    @Resource
    private SubjectCategoryService subjectCategoryService;

    @Override
    public Boolean add(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectLabelBO));
        }
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE
                .convertBoToLabel(subjectLabelBO);
        subjectLabel.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        int count = subjectLabelService.insert(subjectLabel);
        return count > 0;
    }

    @Override
    public Boolean update(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.update.bo:{}", JSON.toJSONString(subjectLabelBO));
        }
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE
                .convertBoToLabel(subjectLabelBO);
        int count = subjectLabelService.update(subjectLabel);
        return count > 0;
    }

    @Override
    public Boolean delete(SubjectLabelBO subjectLabelBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectLabelDomainServiceImpl.update.bo:{}", JSON.toJSONString(subjectLabelBO));
        }
        SubjectLabel subjectLabel = SubjectLabelConverter.INSTANCE
                .convertBoToLabel(subjectLabelBO);
        subjectLabel.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        int count = subjectLabelService.update(subjectLabel);
        return count > 0;
    }

    @Override
    public List<SubjectLabelBO> queryLabelByCategoryId(SubjectLabelBO subjectLabelBO) {
        Long categoryId = subjectLabelBO.getCategoryId();
        SubjectCategory subjectCategory = subjectCategoryService.queryById(categoryId);
        if(subjectCategory.getCategoryType() == CategoryTypeEnum.PRIMARY.getCode()){
            SubjectLabel subjectLabel = new SubjectLabel();
            subjectLabel.setCategoryId(categoryId);
            List<SubjectLabel> subjectLabelList =subjectLabelService.queryByCondition(subjectLabel);
            List<SubjectLabelBO> subjectLabelBOS = SubjectLabelConverter.INSTANCE.convertToBOList(subjectLabelList);
            return subjectLabelBOS;
        }
        SubjectMapping subjectMapping = new SubjectMapping();
        subjectMapping.setCategoryId(categoryId);
        subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        List<SubjectMapping> subjectMappingList = subjectMappingService.queryByLabelId(subjectMapping);
        if(CollectionUtils.isEmpty(subjectMappingList)){
            return Collections.emptyList();
        }
        List<Long> labelIdList = subjectMappingList.stream().map(SubjectMapping::getLabelId)
                .collect(Collectors.toList());
        List<SubjectLabel> labelList = subjectLabelService.batchQueryByIds(labelIdList);
        List<SubjectLabelBO> boList = labelList.stream().map(label -> {
            SubjectLabelBO labelBO = new SubjectLabelBO();
            labelBO.setLabelName(label.getLabelName());
            labelBO.setCategoryId(categoryId);
            labelBO.setId(label.getId());
            labelBO.setSortNum(label.getSortNum());
            return labelBO;
        }).collect(Collectors.toList());
        return boList;
    }
}
