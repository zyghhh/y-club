package com.yclub.auth.api;

import com.yclub.auth.entity.AuthUserDTO;
import com.yclub.auth.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-20  0:12
 */

@FeignClient("y-club-auth-dev")
public interface UserFeignService {
    @RequestMapping("/user/getUserInfo")
    public Result<AuthUserDTO> getUserInfo(@RequestBody AuthUserDTO authUserDTO) ;

    @RequestMapping("/user/batchGetUserInfo")
    Result<List<AuthUserDTO>> batchGetUserInfo(List<AuthUserDTO> authUserDTOList);
}
