package com.blog.entity;

import com.blog.entity.core.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 点赞记录
 * </p>
 *
 * @author Graydon
 * @since 2019-03-22
 */
@Getter
@Setter
@ToString
public class PraiseLog extends Entity {

        /**
     * 被点赞的评论ID
     */
         private Long commentId;

        /**
     * 帖子ID
     */
         private Long postId;

        /**
     * 点赞的用户ID
     */
         private Long userId;
}
