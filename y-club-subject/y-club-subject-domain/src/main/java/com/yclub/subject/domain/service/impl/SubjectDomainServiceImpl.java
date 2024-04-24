package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.common.enums.IsDeletedFlagEnum;
import com.yclub.subject.common.util.IdWorkerUtil;
import com.yclub.subject.domain.convert.SubjectInfoConverter;
import com.yclub.subject.domain.entity.SubjectInfoBO;
import com.yclub.subject.domain.entity.SubjectOptionBO;
import com.yclub.subject.domain.handler.subject.SubjectTypeHandler;
import com.yclub.subject.domain.handler.subject.SubjectTypeHandlerFactory;
import com.yclub.subject.domain.service.SubjectDomainService;
import com.yclub.subject.infra.basic.entity.*;
import com.yclub.subject.infra.basic.rpc.UserRpc;
import com.yclub.subject.infra.basic.service.SubjectEsService;
import com.yclub.subject.infra.basic.service.SubjectInfoService;
import com.yclub.subject.infra.basic.service.SubjectLabelService;
import com.yclub.subject.infra.basic.service.SubjectMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    SubjectEsService subjectEsService;

    @Resource
    UserRpc userRpc;


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

        // add 题目要同步到ES
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setDocId(new IdWorkerUtil(1, 1, 1).nextId());
        subjectInfoEs.setSubjectId(subjectInfo.getId());
        subjectInfoEs.setSubjectAnswer(subjectInfoBO.getSubjectAnswer());
        subjectInfoEs.setCreateTime(new Date().getTime());
        //subjectInfoEs.setCreateUser(LoginUtil.getLoginId());
        subjectInfoEs.setCreateUser("zyg");
        subjectInfoEs.setSubjectName(subjectInfoBO.getSubjectName());
        subjectInfoEs.setSubjectType(subjectInfoBO.getSubjectType());
        subjectEsService.insert(subjectInfoEs);
    }

    @Override
    public PageResult<SubjectInfoBO> getSubjectPage(SubjectInfoBO subjectInfoBO) {
        PageResult<SubjectInfoBO> pageResult = new PageResult<>();
        pageResult.setPageNo(subjectInfoBO.getPageNo());
        pageResult.setPageSize(subjectInfoBO.getPageSize());
        int start = (subjectInfoBO.getPageNo() - 1) * subjectInfoBO.getPageSize();
        SubjectInfo subjectInfo = SubjectInfoConverter.INSTANCE.convertBOToSubjectInfo(subjectInfoBO);
        int count = subjectInfoService.countByCondition(subjectInfo, subjectInfoBO.getCategoryId()
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

    @Override
    public PageResult<SubjectInfoEs> getSubjectPageBySearch(SubjectInfoBO subjectInfoBO) {
        if (log.isInfoEnabled()) {
            log.info("SubjectDomainServiceImpl.getSubjectPageBySearch.bo:{}", JSON.toJSONString(subjectInfoBO));
        }
        SubjectInfoEs subjectInfoEs = new SubjectInfoEs();
        subjectInfoEs.setKeyWord(subjectInfoBO.getKeyWord());
        subjectInfoEs.setPageNo(subjectInfoBO.getPageNo());
        subjectInfoEs.setPageSize(subjectInfoBO.getPageSize());
        return subjectEsService.querySubjectList(subjectInfoEs);
    }

    @Override
    public List<SubjectInfoBO> getContributeList() {
        List<SubjectInfo> subjectInfoList = subjectInfoService.getContributeCount();
        if(CollectionUtils.isEmpty(subjectInfoList)){
            return Collections.emptyList();
        }
        List<SubjectInfoBO> subjectInfoBOList = new LinkedList<>();
        subjectInfoList.forEach(subjectInfo -> {
            SubjectInfoBO subjectInfoBO = new SubjectInfoBO();
            subjectInfoBO.setSubjectCount(subjectInfo.getSubjectCount());
             UserInfo userInfo = userRpc.getUserInfo(subjectInfo.getCreatedBy());
             subjectInfoBO.setCreateUser(userInfo.getNickName());
             subjectInfoBO.setCreateUserAvatar(userInfo.getAvatar());
             subjectInfoBOList.add(subjectInfoBO);
        });
        //按照贡献数排序
        subjectInfoBOList.sort(((o1, o2) -> o2.getSubjectCount() - o1.getSubjectCount()));
        return subjectInfoBOList;
    }

}