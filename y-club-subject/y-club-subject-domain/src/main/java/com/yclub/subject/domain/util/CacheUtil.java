package com.yclub.subject.domain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-21  10:26
 */
@Component
public class CacheUtil<V> {
    private static Cache<String,String> localCache = CacheBuilder
            .newBuilder().maximumSize(5000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public static  <V>  List<V> getListResult(String key,Class<V> clazz, Function<String, List<V>> function) {
        String content = localCache.getIfPresent(key);
        List<V> resultList;
        if (StringUtils.isNotBlank(content)) {
            resultList = JSON.parseArray(content,clazz);
        } else{
            resultList = function.apply(key);
            if (!CollectionUtils.isEmpty(resultList)) {
                localCache.put(key, JSON.toJSONString(resultList));
            }
        }
        return resultList;
    }

    /**
     * 从本地缓存中获取或计算并存储键值对映射结果。
     *
     * @param <K>       映射键的类型
     * @param <V>       映射值的类型
     * @param cacheKey  缓存键
     * @param valueClass 值的类类型
     * @param mapperFunction 将键转换为映射结果的函数
     * @return          返回映射结果
     */
    public static  <K, V> Map<K, V> getMappedResult(String cacheKey, Class<V> valueClass, Function<String, Map<K, V>> mapperFunction) {
        String content = localCache.getIfPresent(cacheKey);
        Map<K, V> resultMap;
        if (StringUtils.isNotBlank(content)) {
            // 如果缓存中有内容，解析JSON字符串为Map对象
            resultMap = JSON.parseObject(content, new TypeReference<Map<K, V>>() {});
        } else {
            // 否则调用提供的函数计算映射结果
            resultMap = mapperFunction.apply(cacheKey);
            if (!resultMap.isEmpty()) {
                // 若计算得到非空映射结果，将其序列化为JSON字符串并放入缓存
                localCache.put(cacheKey, JSON.toJSONString(resultMap));
            }
        }
        return resultMap;
    }
}
