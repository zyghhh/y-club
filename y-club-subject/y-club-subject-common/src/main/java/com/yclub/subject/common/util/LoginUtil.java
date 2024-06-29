package com.yclub.subject.common.util;

import com.yclub.subject.common.context.LoginContextHolder;
/**
 * 用户登录util
 *
 * @author: zyg
 * @date: 2023/11/26
 */
public class LoginUtil {

    public static String getLoginId() {
        return LoginContextHolder.getLoginId();
    }


}
