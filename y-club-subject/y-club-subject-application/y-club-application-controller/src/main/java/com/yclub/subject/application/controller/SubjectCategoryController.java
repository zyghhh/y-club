package com.yclub.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.yclub.subject.application.convert.SubjectCategoryDTOConverter;
import com.yclub.subject.application.convert.SubjectLabelDTOConverter;
import com.yclub.subject.application.dto.SubjectCategoryDTO;
import com.yclub.subject.application.dto.SubjectLabelDTO;
import com.yclub.subject.common.entity.Result;
import com.yclub.subject.domain.convert.SubjectLabelConverter;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.service.SubjectCategoryDomainService;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import com.yclub.subject.infra.basic.entity.SubjectLabel;
import com.yclub.subject.infra.basic.service.SubjectCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-03-29  16:49
 */
@RestController
@RequestMapping("/subject/category")
@Slf4j
public class SubjectCategoryController {
    @Resource
    private SubjectCategoryDomainService subjectCategoryDomainService;

    @Resource
    private SubjectCategoryService subjectCategoryService;

    @RequestMapping("/add")
    public Result<Boolean> test(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.add.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getParentId(), "parentId is null");
            Preconditions.checkNotNull(subjectCategoryDTO.getCategoryType(), "CategoryType is null");
            Preconditions.checkArgument(!StringUtils.isBlank(subjectCategoryDTO.getCategoryName()), "CategoryName is null");
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE
                    .convertDTOToBOCategory(subjectCategoryDTO);
            subjectCategoryDomainService.add(subjectCategoryBO);
            return Result.ok(true);
        } catch (Exception e) {
            log.error("SubjectCategoryController.add.error:{}", e.getMessage(), e);
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询岗位大类
     */
    @PostMapping("/queryPrimaryCategory")
    public Result<List<SubjectCategoryDTO>> queryPrimaryCategory(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE.
                    convertDTOToBOCategory(subjectCategoryDTO);
            List<SubjectCategoryBO> subjectCategoryBOList = subjectCategoryDomainService.queryCategory(subjectCategoryBO);
            List<SubjectCategoryDTO> subjectCategoryDTOList = SubjectCategoryDTOConverter.INSTANCE.
                    convertBoToCategoryDTOList(subjectCategoryBOList);
            return Result.ok(subjectCategoryDTOList);
        } catch (Exception e) {
            log.error("SubjectCategoryController.queryPrimaryCategory.error:{}", e.getMessage(), e);
            return Result.fail("查询失败");
        }

    }

    /**
     * 根据分类id查二级分类
     */
    @PostMapping("/queryCategoryByPrimary")
    public Result<List<SubjectCategoryDTO>> queryCategoryByPrimary(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.queryCategoryByPrimary.dto:{}"
                        , JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getParentId(), "分类id不能为空");
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE.convertDTOToBOCategory(subjectCategoryDTO);
            List<SubjectCategoryBO> subjectCategoryBOList = subjectCategoryDomainService.queryCategory(subjectCategoryBO);
            List<SubjectCategoryDTO> subjectCategoryDTOList = SubjectCategoryDTOConverter.INSTANCE.
                    convertBoToCategoryDTOList(subjectCategoryBOList);
            return Result.ok(subjectCategoryDTOList);
        } catch (Exception e) {
            log.error("SubjectCategoryController.queryPrimaryCategory.error:{}", e.getMessage(), e);
            return Result.fail("查询失败");
        }
    }

    /**
     * 根据分类id查二级分类及对应标签
     */
    @PostMapping("/queryCategoryAndLabel")
    public Result<List<SubjectCategoryDTO>> queryCategoryAndLabel(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.queryCategoryAndLabel.dto:{}"
                        , JSON.toJSONString(subjectCategoryDTO));
            }
            Preconditions.checkNotNull(subjectCategoryDTO.getId(), "分类id不能为空");
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE.convertDTOToBOCategory(subjectCategoryDTO);
            List<SubjectCategoryBO> subjectCategoryBOList = subjectCategoryDomainService.queryCategoryAndLabel(subjectCategoryBO);
            List<SubjectCategoryDTO> result = new ArrayList<>();
            subjectCategoryBOList.forEach(subjectCategoryBO1 -> {
                SubjectCategoryDTO subjectCategoryDTO1 = SubjectCategoryDTOConverter.INSTANCE.convertBoToCategoryDTO(subjectCategoryBO1);
                List<SubjectLabelDTO> subjectLabelDTOList = SubjectLabelDTOConverter.INSTANCE.
                        convertBoToLabelDTOList(subjectCategoryBO1.getLabelBOList());
                subjectCategoryDTO1.setLabelList(subjectLabelDTOList);
                result.add(subjectCategoryDTO1);
            });
            return Result.ok(result);
        } catch (Exception e) {
            log.error("SubjectCategoryController.queryCategoryAndLabel.error:{}", e.getMessage(), e);
            return Result.fail("查询失败");
        }
    }

    /**
     * 更新分类
     * 逻辑删除
     */
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.update.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE.convertDTOToBOCategory(subjectCategoryDTO);
            Boolean result = subjectCategoryDomainService.update(subjectCategoryBO);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("SubjectCategoryController.update.error:{}", e.getMessage(), e);
            return Result.fail("更新分类失败");
        }

    }

    /**
     * 删除分类
     */
    @PostMapping("/delete")
    public Result<Boolean> delete(@RequestBody SubjectCategoryDTO subjectCategoryDTO) {
        try {
            if (log.isInfoEnabled()) {
                log.info("SubjectCategoryController.delete.dto:{}", JSON.toJSONString(subjectCategoryDTO));
            }
            SubjectCategoryBO subjectCategoryBO = SubjectCategoryDTOConverter.INSTANCE.convertDTOToBOCategory(subjectCategoryDTO);
            Boolean result = subjectCategoryDomainService.delete(subjectCategoryBO);
            return Result.ok(result);
        } catch (Exception e) {
            log.error("SubjectCategoryController.delete.error:{}", e.getMessage(), e);
            return Result.fail("删除分类失败");
        }

    }


    @RequestMapping("/testDB")
    public String testDB() {
        SubjectCategory subjectCategory = subjectCategoryService.queryById(1L);
        return subjectCategory.getCategoryName();
    }


}
