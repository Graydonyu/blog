package com.blog.entity.core;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.util.Date;

/**
 * Created by yuguidong on 2018/9/21.
 */
@Setter
@Getter
public class Entity extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private String createUser;

    private Date gmtCreate;

    private String modifiedUser;

    private Date gmtModified;
}
