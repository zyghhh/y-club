package com.yclub.subject.common.enums;

import com.yclub.subject.common.entity.Result;
import lombok.Getter;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  14:40
 */

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"success"),
    FAIL(500,"fail");

    private final int code;
    private final String desc;

    private ResultCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ResultCodeEnum getByCode(int code){
        for(ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()){
            if(resultCodeEnum.code == code){
                return resultCodeEnum;
            }
        }
        return null;
    }
}
