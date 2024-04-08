package com.yclub.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.yclub.subject.application.convert.SubjectAnswerDTOConverter;
import com.yclub.subject.application.convert.SubjectDTOConverter;
import com.yclub.subject.application.dto.SubjectInfoDTO;
import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.common.entity.Result;
import com.yclub.subject.domain.entity.SubjectAnswerBO;
import com.yclub.subject.domain.entity.SubjectInfoBO;
import com.yclub.subject.domain.service.SubjectDomainService;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-03-29  16:49
 */
@RestController
@RequestMapping("/subject")
@Slf4j
public class SubjectController {
    @Resource
    private SubjectDomainService subjectDomainService;

    @Resource
    private SubjectCategoryService subjectCategoryService;

    /*
     * 新增题目
     */
    @RequestMapping("/add")
    public Result<Boolean> add(@RequestBody SubjectInfoDTO subjectInfoDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectController.add.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            Preconditions.checkArgument(!StringUtils.isBlank(subjectInfoDTO.getSubjectName()), "SubjectName is null");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(subjectInfoDTO.getCategoryIds()), "CategoryIds is null");
            Preconditions.checkArgument(!CollectionUtils.isEmpty(subjectInfoDTO.getLabelIds()), "LabelIds is null");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectDifficult(), "SubjectDifficult is null");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectType(), "SubjectType is null");
            Preconditions.checkNotNull(subjectInfoDTO.getSubjectScore(), "SubjectScore is null");

            SubjectInfoBO subjectInfoBO = SubjectDTOConverter.INSTANCE.convertDTOToBO(subjectInfoDTO);
            List<SubjectAnswerBO> subjectAnswerBOS = SubjectAnswerDTOConverter.INSTANCE.
                    convertDTOToBOList(subjectInfoDTO.getOptionList());
            subjectInfoBO.setOptionList(subjectAnswerBOS);
            subjectDomainService.add(subjectInfoBO);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("SubjectController.add.error:{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询题目列表
     */
    @PostMapping("/getSubjectPage")
    public Result<PageResult<SubjectInfoDTO>> getSubjectPage(@RequestBody SubjectInfoDTO subjectInfoDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectController.  .dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            Preconditions.checkNotNull(subjectInfoDTO.getCategoryId(), "分类id不能为空");
            Preconditions.checkNotNull(subjectInfoDTO.getLabelId(), "标签id不能为空");
            SubjectInfoBO subjectInfoBO = SubjectDTOConverter.INSTANCE.convertDTOToBO(subjectInfoDTO);
            subjectInfoBO.setPageNo(subjectInfoDTO.getPageNo());
            subjectInfoBO.setPageSize(subjectInfoDTO.getPageSize());
            PageResult<SubjectInfoBO> boPageResult = subjectDomainService.getSubjectPage(subjectInfoBO);
            return Result.ok(boPageResult);
        } catch (Exception e) {
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(), e);
            return Result.fail("分页查询题目失败");
        }
    }

    /**
     * 查询题目信息
     */
    @PostMapping("/querySubjectInfo")
    public Result<SubjectInfoDTO> querySubjectInfo(@RequestBody SubjectInfoDTO subjectInfoDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectController.querySubjectInfo.dto:{}", JSON.toJSONString(subjectInfoDTO));
            }
            Preconditions.checkNotNull(subjectInfoDTO.getId(), "题目id不能为空");
            SubjectInfoBO subjectInfoBO = SubjectDTOConverter.INSTANCE.convertDTOToBO(subjectInfoDTO);
            SubjectInfoBO boResult = subjectDomainService.querySubjectInfo(subjectInfoBO);
            SubjectInfoDTO dto = SubjectDTOConverter.INSTANCE.convertBoToSubjectInfoDTO(boResult);
            return Result.ok(dto);
        } catch (Exception e) {
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(), e);
            return Result.fail("查询题目详情失败");
        }
    }


}
