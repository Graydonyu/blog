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
@TableName("user_message")
public class UserMessage extends Entity {

        /**
     * 发送消息的用户ID
     */
         private Long fromUserId;

        /**
     * 接收消息的用户ID
     */
         private Long toUserId;

        /**
     * 消息可能关联的帖子
     */
         private Long postId;

        /**
     * 消息可能关联的评论
     */
         private Long commentId;

    private String content;

        /**
     * 消息类型
     */
         private Integer type;
}
