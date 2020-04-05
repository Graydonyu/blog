package com.blog.shiro;

import com.blog.entity.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AccountProfile extends BaseEntity {

    private String username;

    private String email;

    private Integer point;

    private String gender;

    private String avatar;

    private Integer postCount;

    private Integer commentCount;

    private String vipLevel;

    private Date lasted;

    private Integer continuousSigninDays;

    private Date firstSigninDay;

    private Date lastSigninDay;
}
