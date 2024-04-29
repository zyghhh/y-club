package com.yclub.practice.server.rpc;

import com.yclub.auth.api.UserFeignService;
import com.yclub.auth.entity.AuthUserDTO;
import com.yclub.auth.entity.Result;
import com.yclub.practice.server.entity.dto.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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


    public List<UserInfo> getUserInfoList(List<String> userNameList) {
        List<UserInfo> userInfoList = new ArrayList<>();
        List<AuthUserDTO> authUserDTOList = new ArrayList<>();
        userNameList.forEach(userName -> {
            AuthUserDTO authUserDTO = new AuthUserDTO();
            authUserDTO.setUserName(userName);
            authUserDTOList.add(authUserDTO);
        });

        Result<List<AuthUserDTO>> result = userFeignService.batchGetUserInfo(authUserDTOList);
        if(!result.getSuccess()){
            return null;
        }
        List<AuthUserDTO> data = result.getData();
        data.forEach(dto -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(dto.getUserName());
            userInfo.setNickName(dto.getNickName());
            userInfo.setAvatar(dto.getAvatar());
            userInfoList.add(userInfo);
        });
        return userInfoList;
    }
}
