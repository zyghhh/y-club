package com.yclub.practice.server.dao;

import com.yclub.practice.server.entity.dto.CategoryDTO;
import com.yclub.practice.server.entity.po.CategoryPO;
import com.yclub.practice.server.entity.po.PrimaryCategoryPO;

import java.util.List;

/**
 * 题目分类(SubjectCategory)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-30 08:38:12
 */
public interface SubjectCategoryDao {
    List<PrimaryCategoryPO> getPrimaryCategory(CategoryDTO categoryDTO);

    CategoryPO selectById(Long id);

    List<CategoryPO> selectList(CategoryDTO categoryDTOTemp);

}

