package com.yclub.subject.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目分类(SubjectCategory)实体类
 *
 * @author makejava
 * @since 2024-03-30 08:38:16
 */
@Data
public class SubjectCategoryBO implements Serializable {
/**
     * 主键
     */
    private Long id;
/**
     * 分类名称
     */
    private String categoryName;
/**
     * 分类类型
     */
    private Integer categoryType;
/**
     * 图标连接
     */
    private String imageUrl;
/**
     * 父级id
     */
    private Long parentId;


}

