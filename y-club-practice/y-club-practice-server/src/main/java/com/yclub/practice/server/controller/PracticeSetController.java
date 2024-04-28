package com.yclub.practice.server.controller;

import com.alibaba.fastjson.JSON;
import com.yclub.practice.api.common.Result;
import com.yclub.practice.api.vo.SpecialPracticeVO;
import com.yclub.practice.server.service.PracticeSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试controller启动问题
 *
 * @author: zyg
 * @date: 2024/3/2
 */
@RestController
@RequestMapping("/practice/set")
@Slf4j
public class PracticeSetController {
    @Resource
    private PracticeSetService practiceSetService;
    @RequestMapping("getSpecialPracticeContent")
    public Result<List<SpecialPracticeVO>> getSpecialPracticeContent() {
        try {
            List<SpecialPracticeVO> result = practiceSetService.getSpecialPracticeContent();
            if (log.isInfoEnabled()) {
                log.info("PracticeSetController.getSpecialPracticeContent.result:{}", JSON.toJSONString(result));
            }
            return Result.ok(result);
        } catch (Exception e) {
            log.error("getSpecialPracticeContent.error:{}", e.getMessage(), e);
            return Result.fail("获取专项练习内容失败");
        }

    }

}
