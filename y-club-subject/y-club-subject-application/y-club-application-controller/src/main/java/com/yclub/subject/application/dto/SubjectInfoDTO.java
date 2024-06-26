package com.yclub.subject.application.dto;

import com.yclub.subject.common.entity.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目信息表(SubjectInfo)实体类
 *
 * @author makejava
 * @since 2024-04-03 10:25:08
 */
@Data
public class SubjectInfoDTO extends PageInfo implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 题目名称
     */
    private String subjectName;
    /**
     * 题目难度
     */
    private Integer subjectDifficult;
    /**
     * 出题人名
     */
    private String settleName;
    /**
     * 题目类型 1单选 2多选 3判断 4简答
     */
    private Integer subjectType;
    /**
     * 题目分数
     */
    private Integer subjectScore;
    /**
     * 题目解析
     */
    private String subjectParse;
    /**
     * 题目答案
     */
    private String subjectAnswer;

    /**
     * 分类id
     */
    private List<Long> categoryIds;

    /**
     * 标签id
     */
    private List<Long> labelIds;

    /**
     * 标签name
     */
    private List<String> labelName;

    /**
     * 查询标签id
     */
    private Long labelId;

    /**
     * 查询分类id
     */
    private Long categoryId;

    /**
     * 答案选项
     */
    private List<SubjectAnswerDTO> optionList;

    /**
     * 全文检索 关键词
     */
    private String keyWord;

    /**
     * 创建人昵称
     */
    private String createUser;

    /**
     * 创建人头像
     */
    private String createUserAvatar;

    /**
     * 题目数量
     */
    private Integer subjectCount;

    /**
     * 当前题目点赞数量
     */
    private Integer likedCount;

    /**
     * 是否被当前用户点赞
     */
    private Boolean liked;

    /**
     * 下一题
     */
    private Long nextSubjectId;

    /**
     * 上一题
     */
    private Long lastSubjectId;

    private Integer isDeleted;

}

