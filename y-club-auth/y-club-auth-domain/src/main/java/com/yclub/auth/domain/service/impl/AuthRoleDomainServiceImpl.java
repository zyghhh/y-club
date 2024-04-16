package com.yclub.auth.domain.service.impl;

import com.yclub.auth.common.enums.IsDeletedFlagEnum;
import com.yclub.auth.domain.convert.AuthRoleBOConverter;
import com.yclub.auth.domain.entity.AuthRoleBO;
import com.yclub.auth.domain.service.AuthRoleDomainService;
import com.yclub.basic.entity.AuthRole;
import com.yclub.basic.service.AuthRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-16  15:39
 */
@Service
@Slf4j
public class AuthRoleDomainServiceImpl implements AuthRoleDomainService {
    @Resource
    private AuthRoleService authRoleService;

    @Override
    public Boolean add(AuthRoleBO authRoleBO) {
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.convertBOToEntity(authRoleBO);
        authRole.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        return authRoleService.insert(authRole) > 0;
    }

    @Override
    public Boolean update(AuthRoleBO authRoleBO) {
        AuthRole authRole = AuthRoleBOConverter.INSTANCE.convertBOToEntity(authRoleBO);
        return authRoleService.update(authRole) > 0;
    }

    @Override
    public Boolean delete(AuthRoleBO authRoleBO) {
        AuthRole authRole = new AuthRole();
        authRole.setId(authRoleBO.getId());
        authRole.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        Integer count = authRoleService.update(authRole);
        return count > 0;
    }
}
