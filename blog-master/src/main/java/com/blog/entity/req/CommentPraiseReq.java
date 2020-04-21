package com.blog.entity.req;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CommentPraiseReq implements Serializable {

    private static final long serialVersionUID = 43L;

    private Long id;

    private boolean ok;
}
