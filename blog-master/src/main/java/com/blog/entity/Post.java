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
 * @since 2019-03-22
 */
@Getter
@Setter
@ToString
public class Post extends Entity {

        /**
     * 标题
     */
         private String title;

        /**
     * 内容
     */
         private String content;

        /**
     * 编辑模式：html可视化，markdown ..
     */
         private String editMode;

    private Long categoryId;

        /**
     * 用户ID
     */
         private Long userId;

        /**
     * 支持人数
     */
         private Integer voteUp;

        /**
     * 反对人数
     */
         private Integer voteDown;

        /**
     * 访问量
     */
         private Integer viewCount;

        /**
     * 评论数量
     */
         private Integer commentCount;

        /**
     * 是否为精华
     */
         private Boolean recommend;

        /**
     * 置顶等级
     */
         private Integer level;

        /**
     * 状态
     */
         private Integer status;
}
