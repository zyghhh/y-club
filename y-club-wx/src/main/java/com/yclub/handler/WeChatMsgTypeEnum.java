package com.yclub.handler;

import lombok.Getter;

/**
 * category枚举
 *
 * @author: ChickenWing
 * @date: 2023/10/3
 */
@Getter
public enum WeChatMsgTypeEnum {

    SUBSCRIBE("event.subscribe", "用户关注事件"),
    TEXT_MSG("text", "接收用户文本消息");

    private final String msgType;

    private final String desc;

    WeChatMsgTypeEnum(String msgType, String desc) {
        this.msgType = msgType;
        this.desc = desc;
    }

    public static WeChatMsgTypeEnum getByCode(String codeVal) {
        for (WeChatMsgTypeEnum resultCodeEnum : WeChatMsgTypeEnum.values()) {
            if (resultCodeEnum.msgType.equals(codeVal)) {
                return resultCodeEnum;
            }
        }
        return null;
    }

}
