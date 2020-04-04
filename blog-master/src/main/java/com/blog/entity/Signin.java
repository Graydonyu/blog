package com.blog.entity;


import com.blog.entity.core.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Signin extends Entity {

    /**
     * 签到的用户ID
     */
    private Long userId;

    /**
     * 获取的飞吻数
     */
    private Integer accumulatePoints;

    /**
     * 签到日期
     */
    private Date signinDate;

    /**
     * 签到时间
     */
    private Date signinTime;
}
