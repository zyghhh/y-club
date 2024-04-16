package com.yclub.basic.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色表(AuthUserRole)实体类
 *
 * @author makejava
 * @since 2024-04-16 10:20:22
 */
@Data
public class AuthUserRole implements Serializable {
    private static final long serialVersionUID = -99736193372652463L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;

    private Integer isDeleted;
}

