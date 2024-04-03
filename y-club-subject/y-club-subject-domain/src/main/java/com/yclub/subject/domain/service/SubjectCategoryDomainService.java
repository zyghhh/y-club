package com.yclub.subject.domain.service;

import com.yclub.subject.domain.entity.SubjectCategoryBO;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:17
 */
public interface SubjectCategoryDomainService {
    void add(SubjectCategoryBO subjectCategoryBO);

    Boolean update(SubjectCategoryBO subjectCategoryBO);

    List<SubjectCategoryBO> queryCategory(SubjectCategoryBO subjectCategoryBO);

    Boolean delete(SubjectCategoryBO subjectCategoryBO);
}
