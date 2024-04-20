package com.yclub.oss.entity;

import lombok.Data;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  14:48
 */

@Data
public class Result<T> {
    private int code;
    private String message;
    private Boolean success;
    private T data;


    public static Result ok(){
        Result result = new Result();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        result.setSuccess(true);

        return result;
    }

    public static <T> Result ok(T data){
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setMessage(ResultCodeEnum.FAIL.getDesc());
        result.setSuccess(false);

        return result;
    }

    public static <T> Result fail(T data){
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setMessage(ResultCodeEnum.FAIL.getDesc());
        result.setSuccess(false);
        result.setData(data);
        return result;
    }


}
