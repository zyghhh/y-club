package com.yclub.auth.domain.service;


import cn.dev33.satoken.stp.SaTokenInfo;
import com.yclub.auth.domain.entity.AuthUserBO;

/**
 * 用户领域service
 * 
 * @author: ChickenWing
 * @date: 2023/11/1
 */
public interface AuthUserDomainService {

    /**
     * 注册
     */
    Boolean register(AuthUserBO authUserBO);

    SaTokenInfo doLogin(String validCode);

    Boolean update(AuthUserBO authUserBO);

    Boolean delete(AuthUserBO authUserBO);

    AuthUserBO getUserInfo(AuthUserBO authUserBO);
}

