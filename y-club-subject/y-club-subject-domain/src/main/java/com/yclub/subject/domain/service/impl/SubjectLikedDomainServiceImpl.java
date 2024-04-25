package com.yclub.subject.domain.service.impl;

import com.alibaba.fastjson.JSON;
import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.common.enums.IsDeletedFlagEnum;
import com.yclub.subject.common.enums.SubjectLikedStatusEnum;
import com.yclub.subject.common.util.LoginUtil;
import com.yclub.subject.domain.convert.SubjectLikedBOConverter;
import com.yclub.subject.domain.entity.SubjectLikedBO;
import com.yclub.subject.domain.redis.RedisUtil;
import com.yclub.subject.domain.service.SubjectLikedDomainService;
import com.yclub.subject.infra.basic.entity.SubjectInfo;
import com.yclub.subject.infra.basic.entity.SubjectLiked;
import com.yclub.subject.infra.basic.service.SubjectInfoService;
import com.yclub.subject.infra.basic.service.SubjectLikedService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 题目点赞表 领域service实现了
 *
 * @author zyg
 * @since 2024-01-07 23:08:45
 */
@Service
@Slf4j
public class SubjectLikedDomainServiceImpl implements SubjectLikedDomainService {

    @Resource
    private SubjectLikedService subjectLikedService;

    @Resource
    private SubjectInfoService subjectInfoService;

    @Resource
    private RedisUtil redisUtil;

    private static final String SUBJECT_LIKED_KEY = "subject.liked";

    private static final String SUBJECT_LIKED_COUNT_KEY = "subject.liked.count";

    private static final String SUBJECT_LIKED_DETAIL_KEY = "subject.liked.detail";

    @Override
    public void add(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        Integer status = subjectLikedBO.getStatus();
        String subjectId = subjectLikedBO.getSubjectId().toString();
        String likeUserId = subjectLikedBO.getLikeUserId();
        String hashKey = buildSubjectLikedKey(subjectId, likeUserId);
        redisUtil.putHash(SUBJECT_LIKED_KEY,hashKey,status.toString());
        String countKey = redisUtil.buildKey(SUBJECT_LIKED_COUNT_KEY,subjectId);
        String detailKey = redisUtil.buildKey(SUBJECT_LIKED_DETAIL_KEY,subjectId,likeUserId);
        if(SubjectLikedStatusEnum.LIKED.getCode() == status){
            redisUtil.increment(countKey,1);
            redisUtil.set(detailKey,"1");
        } else{
            Integer num = redisUtil.getInt(countKey);
            if(num == 1){
                redisUtil.del(countKey);
            }
            if(Objects.isNull(num) || num <= 0){
                return;
            }
            redisUtil.increment(countKey,-1);
            redisUtil.del(detailKey);
        }
    }

    @Override
    public Boolean isLiked(String subjectId, String likeUserId) {
        String detailKey = redisUtil.buildKey(SUBJECT_LIKED_DETAIL_KEY,subjectId,likeUserId);
        return redisUtil.exist(detailKey);
    }

    @Override
    public Integer getLikedCount(String subjectId) {
        String countKey = redisUtil.buildKey(SUBJECT_LIKED_COUNT_KEY,subjectId);
        Integer count = redisUtil.getInt(countKey);
        if (Objects.isNull(count) || count <= 0) {
            return 0;
        }
        return redisUtil.getInt(countKey);
    }

    private String buildSubjectLikedKey(String subjectId, String userId) {
        return subjectId + ":" + userId;
    }

    @Override
    public void syncLiked() {
        Map<Object,Object> subjectLikedMap = redisUtil.getHashAndDelete(SUBJECT_LIKED_KEY);
        if(MapUtils.isEmpty(subjectLikedMap)){
            return;
        }
        if (log.isInfoEnabled()) {
            log.info("syncLiked.subjectLikedMap:{}", JSON.toJSONString(subjectLikedMap));
        }
        List<SubjectLiked> subjectLikedList = new LinkedList<>();
        subjectLikedMap.forEach((k,v)->{
            String[] split = k.toString().split(":");
            if(split.length != 2){
                return;
            }
            SubjectLiked subjectLiked = new SubjectLiked();
            subjectLiked.setSubjectId(Long.valueOf(split[0]));
            subjectLiked.setLikeUserId(split[1]);
            subjectLiked.setStatus(Integer.valueOf(v.toString()));
            subjectLiked.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
            subjectLikedList.add(subjectLiked);
        });
        List<SubjectLiked> exist = subjectLikedService.queryAll();
        subjectLikedList.forEach(subjectLiked -> {
            exist.forEach(existSubjectLiked -> {
                if (subjectLiked.getSubjectId().equals(existSubjectLiked.getSubjectId()) && subjectLiked.getLikeUserId().equals(existSubjectLiked.getLikeUserId())) {
                    subjectLiked.setId(existSubjectLiked.getId());
                }
            });
        });
        subjectLikedService.batchInsertOrUpdate(subjectLikedList);
    }

    @Override
    public Boolean update(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        return subjectLikedService.update(subjectLiked) > 0;
    }

    @Override
    public Boolean delete(SubjectLikedBO subjectLikedBO) {
        SubjectLiked subjectLiked = new SubjectLiked();
        subjectLiked.setId(subjectLikedBO.getId());
        subjectLiked.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        return subjectLikedService.deleteById(subjectLiked.getId()) ;
    }

    @Override
    public PageResult<SubjectLikedBO> getSubjectLikedPage(SubjectLikedBO subjectLikedBO) {
        PageResult<SubjectLikedBO> pageResult = new PageResult<>();
        pageResult.setPageNo(subjectLikedBO.getPageNo());
        pageResult.setPageSize(subjectLikedBO.getPageSize());
        int start = (subjectLikedBO.getPageNo() - 1) * subjectLikedBO.getPageSize();
        SubjectLiked subjectLiked = SubjectLikedBOConverter.INSTANCE.convertBOToEntity(subjectLikedBO);
        subjectLiked.setLikeUserId(LoginUtil.getLoginId());
        int count = subjectLikedService.countByCondition(subjectLiked);
        if (count == 0) {
            return pageResult;
        }
        List<SubjectLiked> subjectLikedList = subjectLikedService.queryPage(subjectLiked, start,
                subjectLikedBO.getPageSize());
        List<SubjectLikedBO> subjectInfoBOS = SubjectLikedBOConverter.INSTANCE.convertListInfoToBO(subjectLikedList);
        subjectInfoBOS.forEach(info -> {
            SubjectInfo subjectInfo = subjectInfoService.queryById(info.getSubjectId());
            info.setSubjectName(subjectInfo.getSubjectName());
        });
        pageResult.setRecords(subjectInfoBOS);
        pageResult.setTotal(count);
        return pageResult;
    }
}
