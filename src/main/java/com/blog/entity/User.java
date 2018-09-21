package com.blog.entity;

import com.blog.entity.core.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author ygd
 * @since 2018-09-21
 */
@Getter
@Setter
@ToString
public class User extends Entity {

    private static final long serialVersionUID = 1L;

    private Integer age;

    private String name;

    private String email;
}
