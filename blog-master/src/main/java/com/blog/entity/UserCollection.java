package com.blog.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-04-30
 */
@Getter
@Setter
@ToString
@TableName("user_collection")
public class UserCollection extends Entity {

    private Long userId;

    private Long postId;

    private Long postUserId;
}
