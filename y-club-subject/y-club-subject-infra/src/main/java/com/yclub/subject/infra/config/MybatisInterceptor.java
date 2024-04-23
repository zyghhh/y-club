package com.yclub.subject.infra.config;

import com.yclub.subject.common.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @desc: 拦截Sql填充 createBy createTime 等公共字段的拦截器
 * @author: zyg
 * @date: 2024-04-23  10:59
 */

@Component
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
        Object.class})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = args[1];
        if (parameter == null) {
            return invocation.proceed();
        }
        String loginId = LoginUtil.getLoginId();
        if (StringUtils.isBlank(loginId)) {
            invocation.proceed();
        }
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            replaceProperty(parameter, sqlCommandType, loginId);
        }
        return invocation.proceed();
    }

    private void replaceProperty(Object parameter, SqlCommandType sqlCommandType, String loginId) {
        if (parameter instanceof Map) {
            replaceMapProperty((Map<String, Object>) parameter, sqlCommandType, loginId);
        } else {
            replaceEntityProperty(parameter, sqlCommandType, loginId);
        }
    }

    private static void replaceMapProperty(Map<String, Object> parameter, SqlCommandType sqlCommandType, String loginId) {
        if (sqlCommandType == SqlCommandType.INSERT) {
            parameter.put("isDeleted",0);
            parameter.put("createBy", loginId);
            parameter.put("createTime", new Date());
        }
        parameter.put("updateBy", loginId);
        parameter.put("updateTime", new Date());
    }

    private void replaceEntityProperty(Object parameter, SqlCommandType sqlCommandType, String loginId) {
        Field[] fields = getAllFields(parameter);
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object o = field.get(parameter);
                if (Objects.nonNull(o)) {
                    field.setAccessible(false);
                    continue;
                }
                if (sqlCommandType == SqlCommandType.INSERT) {
                    if ("isDeleted".equals(field.getName())) {
                        field.set(parameter, 0);
                    }
                    if ("createdBy".equals(field.getName())) {
                        field.set(parameter, loginId);
                    }
                    if ("createdTime".equals(field.getName())) {
                        field.set(parameter, new Date());
                    }

                }
                if ("updateBy".equals(field.getName())) {
                    field.set(parameter, loginId);
                }
                if ("updateTime".equals(field.getName())) {
                    field.set(parameter, new Date());
                }
                field.setAccessible(false);
            } catch (Exception e) {
                log.error("replaceEntityProperty error", e);
            }
        }

    }

    private Field[] getAllFields(Object object) {
        Class<?> aClass = object.getClass();
        List<Field> fieldList = new LinkedList<>();
        while (aClass != null) {
            Field[] declaredFields = aClass.getDeclaredFields();
            fieldList.addAll(new ArrayList<>(Arrays.asList(declaredFields)));
            aClass = aClass.getSuperclass();
        }
        return fieldList.toArray(new Field[0]);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
