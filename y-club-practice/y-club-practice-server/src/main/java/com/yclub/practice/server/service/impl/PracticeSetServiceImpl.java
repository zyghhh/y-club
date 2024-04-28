package com.yclub.practice.server.service.impl;

import com.yclub.practice.api.enums.SubjectInfoTypeEnum;
import com.yclub.practice.api.vo.SpecialPracticeCategoryVO;
import com.yclub.practice.api.vo.SpecialPracticeLabelVO;
import com.yclub.practice.api.vo.SpecialPracticeVO;
import com.yclub.practice.server.dao.SubjectCategoryDao;
import com.yclub.practice.server.dao.SubjectLabelDao;
import com.yclub.practice.server.dao.SubjectMappingDao;
import com.yclub.practice.server.entity.dto.CategoryDTO;
import com.yclub.practice.server.entity.po.CategoryPO;
import com.yclub.practice.server.entity.po.LabelCountPO;
import com.yclub.practice.server.entity.po.PrimaryCategoryPO;
import com.yclub.practice.server.entity.po.SubjectLabelPO;
import com.yclub.practice.server.service.PracticeSetService;
import javafx.beans.binding.IntegerBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-28  10:20
 */
@Slf4j
@Service
public class PracticeSetServiceImpl implements PracticeSetService {

    @Resource
    SubjectCategoryDao subjectCategoryDao;

    @Resource
    SubjectLabelDao subjectLabelDao;
    @Resource
    SubjectMappingDao subjectMappingDao;
    @Override
    public List<SpecialPracticeVO> getSpecialPracticeContent() {
        List<SpecialPracticeVO> specialPracticeVOList = new ArrayList<>();
        List<Integer> subjectTypeList = new LinkedList<>();
        subjectTypeList.add(SubjectInfoTypeEnum.BRIEF.getCode());
        subjectTypeList.add(SubjectInfoTypeEnum.RADIO.getCode());
        subjectTypeList.add(SubjectInfoTypeEnum.MULTIPLE.getCode());
        subjectTypeList.add(SubjectInfoTypeEnum.JUDGE.getCode());
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSubjectTypeList(subjectTypeList);
        List<PrimaryCategoryPO> primaryCategoryList = subjectCategoryDao.getPrimaryCategory(categoryDTO);
        if(CollectionUtils.isEmpty(primaryCategoryList)){
            return specialPracticeVOList;
        }
        primaryCategoryList.forEach(primaryCategoryPO -> {
            SpecialPracticeVO specialPracticeVO = new SpecialPracticeVO();
            specialPracticeVO.setPrimaryCategoryId(primaryCategoryPO.getParentId());
            CategoryPO categoryPO = subjectCategoryDao.selectById(primaryCategoryPO.getParentId());
            specialPracticeVO.setPrimaryCategoryName(categoryPO.getCategoryName());
            CategoryDTO categoryDTOTemp = new CategoryDTO();
            categoryDTOTemp.setCategoryType(2); //只查子类的
            categoryDTOTemp.setParentId(primaryCategoryPO.getParentId());
            List<CategoryPO> childCategoryPoList = subjectCategoryDao.selectList(categoryDTOTemp);
            if (CollectionUtils.isEmpty(childCategoryPoList)) {
                return;
            }
            List<SpecialPracticeCategoryVO> categoryList = new LinkedList<>();
            childCategoryPoList.forEach(childCategory -> {
                List<SpecialPracticeLabelVO> labelVOList = getLabelVOList(childCategory.getId(), subjectTypeList);
                if (CollectionUtils.isEmpty(labelVOList)) {
                    return;
                }
                SpecialPracticeCategoryVO specialPracticeCategoryVO = new SpecialPracticeCategoryVO();
                specialPracticeCategoryVO.setCategoryId(childCategory.getId());
                specialPracticeCategoryVO.setCategoryName(childCategory.getCategoryName());
                List<SpecialPracticeLabelVO> labelList = new LinkedList<>();
                labelVOList.forEach(labelVo -> {
                    SpecialPracticeLabelVO specialPracticeLabelVO = new SpecialPracticeLabelVO();
                    specialPracticeLabelVO.setId(labelVo.getId());
                    specialPracticeLabelVO.setAssembleId(labelVo.getAssembleId());
                    specialPracticeLabelVO.setLabelName(labelVo.getLabelName());
                    labelList.add(specialPracticeLabelVO);
                });// 重复了？？？ 直接 specialPracticeCategoryVO.setLabelList(labelVOList); 不行吗
                specialPracticeCategoryVO.setLabelList(labelList);
                categoryList.add(specialPracticeCategoryVO);
            });
            specialPracticeVO.setCategoryList(categoryList);
            specialPracticeVOList.add(specialPracticeVO);
        });
        return specialPracticeVOList;
    }

    private List<SpecialPracticeLabelVO> getLabelVOList(Long categoryId, List<Integer> subjectTypeList) {
        List<LabelCountPO> countPOList = subjectMappingDao.getLabelSubjectCount(categoryId, subjectTypeList);
        if (CollectionUtils.isEmpty(countPOList)) {
            return Collections.emptyList();
        }
        List<SpecialPracticeLabelVO> voList = new LinkedList<>();
        countPOList.forEach(countPo -> {
            SpecialPracticeLabelVO vo = new SpecialPracticeLabelVO();
            vo.setId(countPo.getLabelId());
            vo.setAssembleId(categoryId + "-" + countPo.getLabelId());
            SubjectLabelPO subjectLabelPO = subjectLabelDao.queryById(countPo.getLabelId());
            vo.setLabelName(subjectLabelPO.getLabelName());
            voList.add(vo);
        });
        return voList;
    }
}
