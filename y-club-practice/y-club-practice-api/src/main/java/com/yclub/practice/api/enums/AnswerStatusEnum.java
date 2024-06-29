package com.yclub.practice.api.enums;

public enum AnswerStatusEnum {

    /**
     * 错误
     */
    ERROR(0, "错误"),

    /**
     * 正确
     */
    CORRECT(1, "正确");

    private final int code;
    private final String desc;

    private AnswerStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}