package com.yclub.auth.application.controller.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限关联表(AuthRolePermission)实体类
 *
 * @author makejava
 * @since 2024-04-16 10:19:48
 */
@Data
public class AuthRolePermissionDTO implements Serializable {
    private static final long serialVersionUID = 508182463777935063L;

    private Long id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 权限id
     */
    private Long permissionId;
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

