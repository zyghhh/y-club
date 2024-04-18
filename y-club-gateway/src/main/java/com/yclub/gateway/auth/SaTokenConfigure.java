package com.yclub.gateway.auth;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-15  16:59
 */

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 配置类
 * @author click33
 */
@Configuration
public class SaTokenConfigure {
    // 注册 Sa-Token全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    System.out.println("-------- 前端访问path：" + SaHolder.getRequest().getRequestPath());
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
//                    SaRouter.match("/**", "/auth/user/doLogin", r -> StpUtil.checkLogin());
//                    SaRouter.match("/**", "/auth/user/doLogin", r -> StpUtil.checkRole("admin"));

                    // 权限认证 -- 不同模块, 校验不同权限
//                    SaRouter.match("/subject/**", r -> StpUtil.checkRole("normal_user"));
//                    SaRouter.match("/subject/add", r -> StpUtil.checkPermission("subject:add"));


                    // 更多匹配 ...  */
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
//                .setError(e -> {
//                    return SaResult.error(e.getMessage());
//                })
                ;
    }
}
