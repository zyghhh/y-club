package com.yclub.practice.api.enums;

import lombok.Getter;

@Getter
public enum CompleteStatusEnum {

    /**
     * 未完成
     */
    NO_COMPLETE(0, "未完成"),

    /**
     * 已完成
     */
    COMPLETE(1, "已完成");

    private final int code;
    private final String desc;

    private CompleteStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}