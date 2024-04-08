package com.yclub.subject.domain.handler.subject;

import com.yclub.subject.common.enums.SubjectInfoTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-04  0:47
 */
@Component
public class SubjectTypeHandlerFactory implements InitializingBean {

    @Resource
    List<SubjectTypeHandler> subjectTypeHandlerList;

    Map<SubjectInfoTypeEnum,SubjectTypeHandler> handlerMap = new HashMap<>();

    public SubjectTypeHandler getHandler(int subjectType){
        SubjectInfoTypeEnum subjectInfoTypeEnum = SubjectInfoTypeEnum.getByCode(subjectType);
        return handlerMap.get(subjectInfoTypeEnum);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for(SubjectTypeHandler subjectTypeHandler : subjectTypeHandlerList){
            handlerMap.put(subjectTypeHandler.getHandlerType(),subjectTypeHandler);
        }
    }
}
