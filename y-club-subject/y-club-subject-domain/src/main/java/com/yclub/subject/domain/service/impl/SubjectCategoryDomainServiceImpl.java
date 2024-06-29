package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yclub.subject.common.enums.IsDeletedFlagEnum;
import com.yclub.subject.domain.config.ThreadPoolConfig;
import com.yclub.subject.domain.convert.SubjectCategoryConverter;
import com.yclub.subject.domain.convert.SubjectLabelConverter;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.entity.SubjectLabelBO;
import com.yclub.subject.domain.service.SubjectCategoryDomainService;
import com.yclub.subject.domain.util.CacheUtil;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.entity.SubjectLabel;
import com.yclub.subject.infra.basic.entity.SubjectMapping;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import com.yclub.subject.infra.basic.service.SubjectLabelService;
import com.yclub.subject.infra.basic.service.SubjectMappingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
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

    @Resource
    ThreadPoolExecutor labelThreadPool;


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
    @SneakyThrows
    public List<SubjectCategoryBO> queryCategoryAndLabel(SubjectCategoryBO subjectCategoryBO) {
        Long queryId = subjectCategoryBO.getParentId();
        String cacheKey = "categoryAndLabel." + queryId;
        List<SubjectCategoryBO> boList = CacheUtil.getListResult(cacheKey, SubjectCategoryBO.class, (key) -> getSubjectCategoryBOS(queryId));
        return boList;
    }

    private List<SubjectCategoryBO> getSubjectCategoryBOS(Long queryId) {
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setParentId(queryId);
        subjectCategory.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        List<SubjectCategory> subjectCategoryList = subjectCategoryService.queryCategory(subjectCategory);
        List<SubjectCategoryBO> boList = SubjectCategoryConverter.INSTANCE.convertToBOList(subjectCategoryList);
        try {
            // 一期优化  到子类加Label 一次查询
            //getCategoryLabels(boList);

            // 二期采用 线程池并发调用 futuretask 优化
            //getCategoryLabelsByFutureTask(boList);

            //继续优化  采用complatebleFuture
            getCategoryLabelsByCompletableFuture(boList);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (log.isInfoEnabled()) {
            log.info("SubjectCategoryController.queryPrimaryCategory.boList:{}",
                    JSON.toJSONString(boList));
        }
        return boList;
    }

    private void getCategoryLabels(List<SubjectCategoryBO> boList) {
        //再拿到每个 子类的所有标签
        boList.forEach(subjectCategory1 -> {
            SubjectMapping subjectMapping = new SubjectMapping();
            subjectMapping.setCategoryId(subjectCategory1.getId());
           List<SubjectMapping> subjectMappingList =  subjectMappingService.queryByCondition(subjectMapping);
            List<Long> labelIds = subjectMappingList.stream()
                    .map(SubjectMapping::getLabelId).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(labelIds)){
                List<SubjectLabel> subjectLabelList = subjectLabelService.batchQueryByIds(labelIds);
                List<SubjectLabelBO> subjectLabelBOS = SubjectLabelConverter.INSTANCE.convertToBOList(subjectLabelList);
                subjectCategory1.setLabelBOList(subjectLabelBOS);
            }
        });
    }

    private void getCategoryLabelsByFutureTask(List<SubjectCategoryBO> boList) throws ExecutionException, InterruptedException {
        //
        List<FutureTask<Map<Long,List<SubjectLabelBO>>>> futureTaskList = new LinkedList<>();
        boList.forEach(subjectCategoryBO -> {
            FutureTask<Map<Long,List<SubjectLabelBO>>> futureTask = new FutureTask<>(() -> getSubjectLabelMap(subjectCategoryBO));
            futureTaskList.add(futureTask);
            labelThreadPool.submit(futureTask);
        });
        Map<Long, List<SubjectLabelBO>> resultMap = new HashMap<>();
        for (FutureTask<Map<Long,List<SubjectLabelBO>>> futureTask : futureTaskList){
            Map<Long, List<SubjectLabelBO>> labelMap = futureTask.get();
            if(!CollectionUtils.isEmpty(labelMap)){
                resultMap.putAll(labelMap);
            }
        }
        boList.forEach(subjectCategoryBO -> {
            Long id = subjectCategoryBO.getId();
            subjectCategoryBO.setLabelBOList(resultMap.get(id));
        });
    }

    private Map<Long,List<SubjectLabelBO>> getSubjectLabelMap(SubjectCategoryBO subjectCategoryBO) {
        log.info("当前线程名字："+ Thread.currentThread().getName());
        SubjectMapping subjectMapping = new SubjectMapping();
        subjectMapping.setCategoryId(subjectCategoryBO.getId());
        Map<Long,List<SubjectLabelBO>> res = new HashMap<>();
        List<SubjectMapping> subjectMappingList =  subjectMappingService.queryByCondition(subjectMapping);
        List<Long> labelIds = subjectMappingList.stream()
                .map(SubjectMapping::getLabelId).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(labelIds)){
            List<SubjectLabel> subjectLabelList = subjectLabelService.batchQueryByIds(labelIds);
            List<SubjectLabelBO> subjectLabelBOS = SubjectLabelConverter.INSTANCE.convertToBOList(subjectLabelList);
            res.put(subjectCategoryBO.getId(),subjectLabelBOS);
        }
        return res;
    }

    private void getCategoryLabelsByCompletableFuture(List<SubjectCategoryBO> boList) throws ExecutionException, InterruptedException {
        //
        List<CompletableFuture<Map<Long,List<SubjectLabelBO>>>> futureTaskList = new LinkedList<>();
        Map<Long, List<SubjectLabelBO>> resultMap = new HashMap<>();
        List<CompletableFuture<Map<Long, List<SubjectLabelBO>>>> completableFutureList = boList.stream().map(category ->
                CompletableFuture.supplyAsync(() -> getSubjectLabelMap(category), labelThreadPool)
        ).collect(Collectors.toList());
        completableFutureList.forEach(future -> {
            try {
                Map<Long, List<SubjectLabelBO>> map = future.get();
                resultMap.putAll(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        boList.forEach(subjectCategoryBO -> {
            Long id = subjectCategoryBO.getId();
            subjectCategoryBO.setLabelBOList(resultMap.get(id));
        });
    }

}