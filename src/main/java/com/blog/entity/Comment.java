package com.blog.entity;

import com.blog.entity.core.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Getter
@Setter
@ToString
public class Comment extends Entity {

        /**
     * 评论的内容
     */
        @NotBlank(message = "内容不能为空")
         private String content;

        /**
     * 回复的评论ID
     */
         private Long parentId;

        /**
     * 评论的内容ID
     */
        @NotNull(message = "帖子ID不能为空")
         private Long postId;

        /**
     * 评论的用户ID
     */
         private Long userId;

        /**
     * “顶”的数量
     */
         private Integer voteUp;

        /**
     * “踩”的数量
     */
         private Integer voteDown;

        /**
     * 置顶等级
     */
         private Integer level;

        /**
     * 评论的状态
     */
         private Integer status;
}
