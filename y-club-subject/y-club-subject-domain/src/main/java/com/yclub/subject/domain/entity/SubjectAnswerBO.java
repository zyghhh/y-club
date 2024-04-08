package com.yclub.subject.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-03  23:23
 */
@Data
public class SubjectAnswerBO implements Serializable {
    /**
     * 答案选项标识
     */
    private Integer optionType;

    /**
     * 答案
     */
    private String optionContent;

    /**
     * 是否正确
     */
    private Integer isCorrect;
}
