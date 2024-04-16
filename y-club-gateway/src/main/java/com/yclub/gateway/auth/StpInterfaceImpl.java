package com.yclub.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.google.gson.Gson;
import com.yclub.gateway.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-15  16:55
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    private String  authPermissionPrefix = "auth.permission.";
    private String  authRolePrefix = "auth.role.";
    @Resource
    RedisUtil redisUtil;
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String authKey = redisUtil.buildKey(authPermissionPrefix,loginId.toString());
        if(StringUtils.isEmpty(authKey)){
            return Collections.emptyList();
        }
        String authValue = redisUtil.get(authKey);
        return new Gson().fromJson(authValue, List.class);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        String authKey = redisUtil.buildKey(authRolePrefix,loginId.toString());
        if(StringUtils.isEmpty(authKey)){
            return Collections.emptyList();
        }
        String authValue = redisUtil.get(authKey);
        return new Gson().fromJson(authValue, List.class);
    }
}
