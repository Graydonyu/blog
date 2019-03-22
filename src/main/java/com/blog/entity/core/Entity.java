package com.blog.entity.core;

import lombok.Getter;

import java.util.Date;

/**
 * Created by yuguidong on 2018/9/21.
 */
@Getter
public class Entity extends BaseEntity{

    private Date created;

    private Date modified;
}
