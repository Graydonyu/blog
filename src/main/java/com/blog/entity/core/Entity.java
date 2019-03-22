package com.blog.entity.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by yuguidong on 2018/9/21.
 */
@Setter
@Getter
public class Entity extends BaseEntity{

    private Date created;

    private Date modified;
}
