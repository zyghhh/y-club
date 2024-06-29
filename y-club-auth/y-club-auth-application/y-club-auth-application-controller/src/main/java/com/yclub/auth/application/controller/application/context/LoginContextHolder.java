package com.yclub.auth.application.controller.application.context;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-19  17:46
 */
public class LoginContextHolder {
    private static final InheritableThreadLocal<Map<String,Object >> THREAD_LOCAL =
            new InheritableThreadLocal<>();

    public static void set(String key, Object val){
        Map<String,Object> map = getThreadLocalMap();
        map.put(key,val);
    }


    public static Map<String, Object> getThreadLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (Objects.isNull(map)) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static Object get(String key){
        Map<String,Object> map = getThreadLocalMap();
        return map.get(key);
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }

    public static String getLoginId(){
        if(Objects.isNull(getThreadLocalMap().get("loginId"))){
            return null;
        }
        return (String)getThreadLocalMap().get("loginId");
    }
}
