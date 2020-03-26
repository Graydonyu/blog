package com.blog.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuguidong on 2019/6/14.
 */
@Data
@Document(indexName = "post", type = "post")
public class PostDocument implements Serializable {
    @Id
    private Long id;

    //使用ik中文分词器，计算生成索引时尽量分细，查找时分词尽量粗粒度
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_smart")
    private String description;

    private Long authorId;
    @Field(type = FieldType.Keyword)
    private String author;
    private String authorVip;
    private String avatar;

    private Long categoryId;
    @Field(type = FieldType.Keyword)
    private String category;

    private Boolean recommend;
    private Integer level;

    @Field(type = FieldType.Text)
    private String tags;

    private Integer commentCount;
    private Integer viewCount;

    @Field(type = FieldType.Date)
    private Date created;

    private Integer status;
}
