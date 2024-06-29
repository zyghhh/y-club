package com.yclub.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-17  20:58
 */
@Component
public class MsgTypeFactory implements InitializingBean {
    @Resource
    List<WeChatMsgHandler> weChatMsgHandlerList;
    private Map<WeChatMsgTypeEnum, WeChatMsgHandler> handlerMap = new HashMap<>();

    public WeChatMsgHandler getHandler(String msgType) {
        WeChatMsgTypeEnum msgTypeEnum = WeChatMsgTypeEnum.getByCode(msgType);
        return handlerMap.get(msgTypeEnum);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (WeChatMsgHandler weChatMsgHandler : weChatMsgHandlerList) {
            handlerMap.put(weChatMsgHandler.getMsgTypeEnum(), weChatMsgHandler);
        }
    }
}
