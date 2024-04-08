package com.yclub.subject.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-07  15:15
 */
@Data
public class SubjectOptionBO implements Serializable {
    /**
     * 题目答案
     */
    private String subjectAnswer;

    /**
     * 答案选项
     */
    private List<SubjectAnswerBO> optionList;
}
