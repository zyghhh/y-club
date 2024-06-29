package com.yclub.subject.infra.basic.service;

import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.infra.basic.entity.SubjectInfoEs;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-22  16:48
 */
public interface SubjectEsService {
    boolean insert(SubjectInfoEs subjectInfoEs);

    PageResult<SubjectInfoEs> querySubjectList(SubjectInfoEs subjectInfoEs);
}
