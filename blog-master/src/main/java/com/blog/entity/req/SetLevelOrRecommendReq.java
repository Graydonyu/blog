package com.blog.entity.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SetLevelOrRecommendReq{

    private Long id;

    private String field;

    private Integer rank;
}
