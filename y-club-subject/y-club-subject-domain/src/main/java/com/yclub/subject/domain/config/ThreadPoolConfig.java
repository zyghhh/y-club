package com.yclub.subject.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @desc: 线程池的config管理
 * @author: zyg
 * @date: 2024-04-19  10:46
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "labelThreadPool")
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(20,100,5, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(40), new CustomNameThreadFactory("label")
                ,new ThreadPoolExecutor.CallerRunsPolicy());
    }




}
