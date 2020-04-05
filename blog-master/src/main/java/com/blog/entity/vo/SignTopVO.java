package com.blog.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class SignTopVO {

    /**
     * 签到的用户ID
     */
    private Long userId;

    /**
     * 连续签到的天数
     */
    private Integer days;

    /**
     * 签到时间
     */
    private Date time;

    /**
     * 签到时间戳
     */
    private Long msec;

    /**
     * 用户的名称和头像
     * */
    private Map<String,Object> user;

    public SignTopVO(){
        user = new HashMap<>();
    }
}
