package com.blog.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by yuguidong on 2018/9/21.
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    // 公共返回结果
    SUCCESS(200, "请求成功"),
    ERROR(500, "服务器错误"),
    NULL(501, "返回空值"),
    NOTICE(300, "提示信息"),
    WARN(301, "警告信息");

    private Integer code;
    private String msg;

}
