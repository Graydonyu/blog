package com.blog.search.dto;

import lombok.Data;

/**
 * 用于服务之间消息通讯模板
 */
@Data
public class PostMqIndexMessage {

    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String REMOVE = "remove";

    public static final int MAX_RETRY = 3;

    private Long postId;
    private String type;
    private int retry = 0;

    public PostMqIndexMessage() {

    }

    public PostMqIndexMessage(Long postId, String type) {
        this.postId = postId;
        this.type = type;
    }

    public PostMqIndexMessage(Long postId, String type, int retry) {
        this.postId = postId;
        this.type = type;
        this.retry = retry;
    }
}