package com.yclub.auth.domain.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.Gson;
import com.yclub.auth.common.enums.AuthUserStatusEnum;
import com.yclub.auth.common.enums.IsDeletedFlagEnum;
import com.yclub.auth.domain.consatnts.AuthConstant;
import com.yclub.auth.domain.convert.AuthUserBOConverter;
import com.yclub.auth.domain.entity.AuthUserBO;
import com.yclub.auth.domain.redis.RedisUtil;
import com.yclub.auth.domain.service.AuthUserDomainService;
import com.yclub.basic.entity.*;
import com.yclub.basic.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthUserDomainServiceImpl implements AuthUserDomainService {

    @Resource
    private AuthUserService authUserService;

    @Resource
    private AuthUserRoleService authUserRoleService;
    @Resource
    private AuthPermissionService authPermissionService;

    @Resource
    private AuthRolePermissionService authRolePermissionService;

    @Resource
    private AuthRoleService authRoleService;

    @Resource
    private RedisUtil redisUtil;

    private String salt = "yclubhhhhh";

    private String authPermissionPrefix = "auth.permission";

    private String authRolePrefix = "auth.role";

    private static final String LOGIN_PREFIX = "loginCode";

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(AuthUserBO authUserBO) {
        //校验用户是否存在
        AuthUser existAuthUser = new AuthUser();
        existAuthUser.setUserName(authUserBO.getUserName());
        List<AuthUser> existUser = authUserService.queryByCondition(existAuthUser);
        if (!existUser.isEmpty()) {
            return true;
        }
        AuthUser authUser = AuthUserBOConverter.INSTANCE.convertBOToEntity(authUserBO);
        if (StringUtils.isNotBlank(authUser.getPassword())) {
            authUser.setPassword(SaSecureUtil.sha256BySalt(authUser.getPassword(), salt));
        }
        if (StringUtils.isBlank(authUser.getAvatar())) {
            authUser.setAvatar("http://117.72.10.84:9000/user/icon/微信图片_20231203153718(1).png");
        }
        authUser.setStatus(AuthUserStatusEnum.OPEN.getCode());
        authUser.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        int count = authUserService.insert(authUser);

        //建立一个初步的角色的关联
        AuthRole authRole = new AuthRole();
        authRole.setRoleKey(AuthConstant.NORMAL_USER);
        AuthRole roleResult = authRoleService.queryByCondition(authRole);
        Long roleId = roleResult.getId();
        Long userId = authUser.getId();
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setUserId(userId);
        authUserRole.setRoleId(roleId);
        authUserRole.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        count = authUserRoleService.insert(authUserRole);


        //存入缓存
        String roleKey = redisUtil.buildKey(authRolePrefix, authUser.getUserName());
        List<AuthRole> roleList = new ArrayList<>();
        roleList.add(authRole);
        redisUtil.set(roleKey,new Gson().toJson(roleList));

        AuthRolePermission authRolePermission = new AuthRolePermission();
        authRolePermission.setRoleId(roleId);
        List<AuthRolePermission> authRolePermissions = authRolePermissionService.queryByCondition(authRolePermission);
        if (!CollectionUtils.isEmpty(authRolePermissions)) {
            List<Long> permissionIdList = authRolePermissions.stream().map(AuthRolePermission::getPermissionId).collect(Collectors.toList());
            List<AuthPermission> authPermissions = authPermissionService.queryByIdList(permissionIdList);
            String permissionKey = redisUtil.buildKey(authPermissionPrefix, authUser.getUserName());
            redisUtil.set(permissionKey,new Gson().toJson(authPermissions));
        }
        return count > 0;
    }

    @Override
    public SaTokenInfo doLogin(String validCode) {
        String key = redisUtil.buildKey(LOGIN_PREFIX, validCode);
        String openId = redisUtil.get(key);
        if(StringUtils.isBlank(openId)){
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        authUserBO.setUserName(openId);
        this.register(authUserBO);
        StpUtil.login(openId);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return tokenInfo;
    }

    @Override
    public Boolean delete(AuthUserBO authUserBO) {
        AuthUser authUser = AuthUserBOConverter.INSTANCE.convertBOToEntity(authUserBO);
        authUser.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        return authUserService.update(authUser) > 0;
    }

    @Override
    @SneakyThrows
    public Boolean update(AuthUserBO authUserBO) {
        AuthUser authUser = AuthUserBOConverter.INSTANCE.convertBOToEntity(authUserBO);
        return authUserService.update(authUser) > 0;
    }

    @Override
    public AuthUserBO getUserInfo(AuthUserBO authUserBO) {
        AuthUser authUser = new AuthUser();
        authUser.setUserName(authUserBO.getUserName());
        List<AuthUser> authUsers = authUserService.queryByCondition(authUser);
        if(authUsers.size() == 1){
            AuthUserBO authUserBO1 = AuthUserBOConverter.INSTANCE.convertEntityToBO(authUsers.get(0));
            return authUserBO1;
        }
        return new AuthUserBO();
    }
}
