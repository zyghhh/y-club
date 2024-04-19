package com.yclub.subject.application.util;

import com.yclub.subject.application.context.LoginContextHolder;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-19  22:50
 */
public class LoginUtil {
    public static String getLoginId(){
        return LoginContextHolder.getLoginId();
    }
}
