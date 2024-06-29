package com.yclub.subject.infra.basic.rpc;

import com.yclub.auth.api.UserFeignService;
import com.yclub.auth.entity.AuthUserDTO;
import com.yclub.auth.entity.Result;
import com.yclub.subject.infra.basic.entity.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-20  19:44
 */
@Component
public class UserRpc {

    @Resource
    UserFeignService userFeignService;

    public UserInfo getUserInfo(String userName){
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setUserName(userName);
        Result<AuthUserDTO> result = userFeignService.getUserInfo(authUserDTO);
        if(!result.getSuccess()){
            return null;
        }
        AuthUserDTO data = result.getData();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(data.getUserName());
        userInfo.setNickName(data.getNickName());
        userInfo.setAvatar(data.getAvatar());
        return userInfo;
    }



}
