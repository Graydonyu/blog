package com.blog.search.service;

import com.blog.search.model.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by yuguidong on 2019/6/17.
 */
public interface SearchService {

    Page<PostDocument> query(Pageable pageable, String keyWord);
}
