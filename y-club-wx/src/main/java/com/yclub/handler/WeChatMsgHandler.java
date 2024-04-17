package com.yclub.handler;

import java.util.Map;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-17  20:55
 */
public interface WeChatMsgHandler {
    WeChatMsgTypeEnum getMsgTypeEnum();

    String dealMsg(Map<String, String> messageMap);


}
