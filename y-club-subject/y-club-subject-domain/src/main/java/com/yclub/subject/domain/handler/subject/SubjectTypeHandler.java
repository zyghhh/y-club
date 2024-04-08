package com.yclub.subject.domain.handler.subject;

import com.yclub.subject.common.enums.SubjectInfoTypeEnum;
import com.yclub.subject.domain.entity.SubjectInfoBO;
import com.yclub.subject.domain.entity.SubjectOptionBO;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-04  0:42
 */
public interface SubjectTypeHandler {
    /**
     * 枚举身份的识别
     */
    SubjectInfoTypeEnum getHandlerType();

    /**
     * 实际的题目的插入
     */
    void add(SubjectInfoBO subjectInfoBO);

    SubjectOptionBO query(Long id);
}
