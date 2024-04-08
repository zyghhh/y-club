package com.yclub.subject.common.enums;

import lombok.Getter;

/**
 * category枚举
 * 
 * @author: ChickenWing
 * @date: 2023/10/3
 */
@Getter
public enum SubjectInfoTypeEnum {

    RADIO(1,"单选"),
    MULTIPLE(2,"多选"),
    JUDGE(3,"判断"),
    BRIEF(4,"简答");

    private final int code;

    private final String desc;

    private SubjectInfoTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static SubjectInfoTypeEnum getByCode(int codeVal){
        for(SubjectInfoTypeEnum resultCodeEnum : SubjectInfoTypeEnum.values()){
            if(resultCodeEnum.code == codeVal){
                return resultCodeEnum;
            }
        }
        return null;
    }

}
