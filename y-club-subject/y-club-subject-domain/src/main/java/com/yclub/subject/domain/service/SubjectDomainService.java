package com.yclub.subject.domain.service;

import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.entity.SubjectInfoBO;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:17
 */
public interface SubjectDomainService {
    /**
     * 新增题目
     */
    void add(SubjectInfoBO subjectInfoBO);

    PageResult<SubjectInfoBO> getSubjectPage(SubjectInfoBO subjectInfoBO);

    SubjectInfoBO querySubjectInfo(SubjectInfoBO subjectInfoBO);
}
