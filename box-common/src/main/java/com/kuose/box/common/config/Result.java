package com.kuose.box.common.config;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

@Getter
public class Result {
    private int status;
    private String info;
    private Map<String, Object> data;

    private static final int SUCCESS_CODE = 1;
    private static final int FAILURE_CODE = 0;

    private Result(int status, String info) {
        this.status = status;
        this.info = info;
        data = Maps.newHashMap();
    }

    public Result(Integer status, String message, Map data) {
        this.status = status;
        this.info = message;
        this.data = data;
    }

    public static Result success() {
        return newResult(SUCCESS_CODE, "请求成功");
    }

    public static Result success(String info) {
        return newResult(SUCCESS_CODE, info);
    }

    public static Result build(Integer status, String message, Map data) {
        return new Result(status, message, data);
    }

    public static Result success(int code, String info) {
        return newResult(code, info);
    }

    public static Result failure() {
        return newResult(FAILURE_CODE, "请求失败");
    }

    public static Result failure(String info) {
        return newResult(FAILURE_CODE, info);
    }

    public static Result failure(int code, String info) {
        return newResult(code, info);
    }

    private static Result newResult(int code, String info) {
        return new Result(code, info);
    }

    public Result setData(String key, Object value) {
        data.put(key, value);
        return this;
    }

}
