package com.yclub.basic.service.impl;

import com.yclub.basic.entity.AuthRolePermission;
import com.yclub.basic.mapper.AuthRolePermissionDao;
import com.yclub.basic.service.AuthRolePermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 角色权限关联表(AuthRolePermission)表服务实现类
 *
 * @author makejava
 * @since 2024-04-16 10:19:48
 */
@Service("authRolePermissionService")
public class AuthRolePermissionServiceImpl implements AuthRolePermissionService {
    @Resource
    private AuthRolePermissionDao authRolePermissionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AuthRolePermission queryById(Long id) {
        return this.authRolePermissionDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param authRolePermission 筛选条件
     * @param pageRequest        分页对象
     * @return 查询结果
     */
    @Override
    public Page<AuthRolePermission> queryByPage(AuthRolePermission authRolePermission, PageRequest pageRequest) {
        long total = this.authRolePermissionDao.count(authRolePermission);
        return new PageImpl<>(this.authRolePermissionDao.queryAllByLimit(authRolePermission, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param authRolePermission 实例对象
     * @return 实例对象
     */
    @Override
    public AuthRolePermission insert(AuthRolePermission authRolePermission) {
        this.authRolePermissionDao.insert(authRolePermission);
        return authRolePermission;
    }

    /**
     * 修改数据
     *
     * @param authRolePermission 实例对象
     * @return 实例对象
     */
    @Override
    public AuthRolePermission update(AuthRolePermission authRolePermission) {
        this.authRolePermissionDao.update(authRolePermission);
        return this.queryById(authRolePermission.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.authRolePermissionDao.deleteById(id) > 0;
    }
}
