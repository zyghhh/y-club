package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.common.enums.IsDeletedFlagEnum;
import com.yclub.subject.domain.convert.SubjectInfoConverter;
import com.yclub.subject.domain.entity.SubjectInfoBO;
import com.yclub.subject.domain.entity.SubjectOptionBO;
import com.yclub.subject.domain.handler.subject.SubjectTypeHandler;
import com.yclub.subject.domain.handler.subject.SubjectTypeHandlerFactory;
import com.yclub.subject.domain.service.SubjectDomainService;
import com.yclub.subject.infra.basic.entity.SubjectInfo;
import com.yclub.subject.infra.basic.entity.SubjectLabel;
import com.yclub.subject.infra.basic.entity.SubjectMapping;
import com.yclub.subject.infra.basic.service.SubjectInfoService;
import com.yclub.subject.infra.basic.service.SubjectLabelService;
import com.yclub.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:17
 */
@Service
@Slf4j
public class SubjectDomainServiceImpl implements SubjectDomainService {
    @Resource
    private SubjectInfoService subjectInfoService;

    @Resource
    private SubjectTypeHandlerFactory subjectTypeHandlerFactory;

    @Resource
    SubjectMappingService subjectMappingService;

    @Resource
    SubjectLabelService subjectLabelService;


    @Override
    @Transactional(rollbackFor = Exception.class)// 设计多表操作 加rollBackFor 用注解控制事务
    public void add(SubjectInfoBO subjectInfoBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectDomainServiceImpl.add.bo:{}", JSON.toJSONString(subjectInfoBO));
        }
        SubjectInfo subjectInfo = SubjectInfoConverter.INSTANCE.convertBOToSubjectInfo(subjectInfoBO);
        subjectInfo.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        subjectInfoService.insert(subjectInfo);
        SubjectTypeHandler handler = subjectTypeHandlerFactory.getHandler(subjectInfoBO.getSubjectType());
        subjectInfoBO.setId(subjectInfo.getId());
        handler.add(subjectInfoBO);
        List<SubjectMapping> subjectMappingList = new ArrayList<>();
        List<Long> categoryIds = subjectInfoBO.getCategoryIds();
        List<Long> labelIds = subjectInfoBO.getLabelIds();
        categoryIds.forEach(categoryId -> {
            labelIds.forEach(labelId -> {
                SubjectMapping subjectMapping = new SubjectMapping();
                subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
                subjectMapping.setCategoryId(categoryId);
                subjectMapping.setLabelId(labelId);
                subjectMapping.setSubjectId(subjectInfoBO.getId());
                subjectMappingList.add(subjectMapping);
            });
        });
        subjectMappingService.batchInsert(subjectMappingList);

    }

    @Override
    public PageResult<SubjectInfoBO> getSubjectPage(SubjectInfoBO subjectInfoBO) {
        PageResult<SubjectInfoBO> pageResult = new PageResult<>();
        pageResult.setPageNo(subjectInfoBO.getPageNo());
        pageResult.setPageSize(subjectInfoBO.getPageSize());
        int start = (subjectInfoBO.getPageNo() - 1) * subjectInfoBO.getPageSize();
        SubjectInfo subjectInfo = SubjectInfoConverter.INSTANCE.convertBOToSubjectInfo(subjectInfoBO);
        int count  = subjectInfoService.countByCondition(subjectInfo, subjectInfoBO.getCategoryId()
                , subjectInfoBO.getLabelId());
        if (count == 0) {
            return pageResult;
        }
        List<SubjectInfo> subjectInfoList = subjectInfoService.queryPage(subjectInfo, subjectInfoBO.getCategoryId()
                , subjectInfoBO.getLabelId(), start, subjectInfoBO.getPageSize());
        List<SubjectInfoBO> subjectInfoBOS = SubjectInfoConverter.INSTANCE.convertToBOList(subjectInfoList);
        pageResult.setRecords(subjectInfoBOS);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public SubjectInfoBO querySubjectInfo(SubjectInfoBO subjectInfoBO) {
        SubjectInfo subjectInfo = subjectInfoService.queryById(subjectInfoBO.getId());
        SubjectTypeHandler handler = subjectTypeHandlerFactory.getHandler(subjectInfo.getSubjectType());
        SubjectOptionBO optionBO = handler.query(subjectInfo.getId());
        SubjectInfoBO bo = SubjectInfoConverter.INSTANCE.convertOptionAndInfoToBo(optionBO, subjectInfo);
        SubjectMapping subjectMapping = new SubjectMapping();
        subjectMapping.setSubjectId(subjectInfo.getId());
        subjectMapping.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        List<SubjectMapping> mappingList = subjectMappingService.queryLabelId(subjectMapping);
        List<Long> labelIdList = mappingList.stream().map(SubjectMapping::getLabelId).collect(Collectors.toList());
        List<SubjectLabel> labelList = subjectLabelService.batchQueryByIds(labelIdList);
        List<String> labelNameList = labelList.stream().map(SubjectLabel::getLabelName).collect(Collectors.toList());
        bo.setLabelName(labelNameList);
        return bo;
    }
}