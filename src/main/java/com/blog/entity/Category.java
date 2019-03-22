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
public class Category extends Entity {

        /**
     * 标题
     */
         private String name;

        /**
     * 内容描述
     */
         private String content;

    private String summary;

        /**
     * 图标
     */
         private String icon;

        /**
     * 该分类的内容数量
     */
         private Integer postCount;

        /**
     * 排序编码
     */
         private Integer orderNum;

        /**
     * 父级分类的ID
     */
         private Long parentId;

        /**
     * SEO关键字
     */
         private String metaKeywords;

        /**
     * SEO描述内容
     */
         private String metaDescription;
}
