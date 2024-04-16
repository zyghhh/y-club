package com.yclub.basic.service.impl;


import com.yclub.basic.entity.AuthUser;
import com.yclub.basic.mapper.AuthUserDao;
import com.yclub.basic.service.AuthUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户信息表(AuthUser)表服务实现类
 *
 * @author makejava
 * @since 2024-04-16 10:18:36
 */
@Service("authUserService")
public class AuthUserServiceImpl implements AuthUserService {
    @Resource
    private AuthUserDao authUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AuthUser queryById(Long id) {
        return this.authUserDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param authUser    筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public Page<AuthUser> queryByPage(AuthUser authUser, PageRequest pageRequest) {
        long total = this.authUserDao.count(authUser);
        return new PageImpl<>(this.authUserDao.queryAllByLimit(authUser, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    @Override
    public Integer insert(AuthUser authUser) {
        return this.authUserDao.insert(authUser);
    }

    /**
     * 修改数据
     *
     * @param authUser 实例对象
     * @return 实例对象
     */
    @Override
    public AuthUser update(AuthUser authUser) {
        this.authUserDao.update(authUser);
        return this.queryById(authUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.authUserDao.deleteById(id) > 0;
    }
}
