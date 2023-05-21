package com.yilan.common;

import java.io.Serializable;

/**
 * @author zhengkang6
 * @version 1.0
 * @since 2023/4/12
 */
public class ResponseResult<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public ResponseResult() {
        this.code = HttpCodeEnum.OK.getCode();
        this.msg = HttpCodeEnum.OK.getEnMessage() + HttpCodeEnum.OK.getZhMessage();
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseResult<?> okResult() {
        return new ResponseResult<>();
    }

    public static ResponseResult<?> okResult(Object data) {
        ResponseResult<Object> result = new ResponseResult<>();
        result.setData(data);
        return result;
    }

    public static ResponseResult<?> okResult(int code, String msg) {
        return new ResponseResult<>(code, msg);
    }
    public static ResponseResult<?> okResult(int code, String msg, Object data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static ResponseResult<?> errorResult(int code, String msg) {
        return new ResponseResult<>(code, msg);
    }

    public static ResponseResult<?> errorResult(HttpCodeEnum enums) {
        return new ResponseResult<>(enums.getCode(), enums.getEnMessage() + enums.getZhMessage());
    }

    public static ResponseResult<?> errorResult(HttpCodeEnum enums, String msg) {
        return new ResponseResult<>(enums.getCode(), msg);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}

