package com.blog.search.service;

import com.blog.search.dto.PostDTO;
import com.blog.search.dto.PostMqIndexMessage;
import com.blog.search.model.PostDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by yuguidong on 2019/6/17.
 */
public interface SearchService {

    Page<PostDocument> query(Pageable pageable, String keyWord);

    void createOrUpdateIndex(PostMqIndexMessage message);

    void removeIndex(PostMqIndexMessage message);

    int initEsIndex(List<PostDTO> postDTOS);
}
