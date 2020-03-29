package com.blog.search.dto;

import lombok.Data;

@Data
public class SearchResultDTO {

    private long current;
    private long size;
    private long total;
    private Object records;

    public SearchResultDTO() {
    }

    public SearchResultDTO(long current, long size, long total) {
        this.current = current;
        this.size = size;
        this.total = total;
    }
}
