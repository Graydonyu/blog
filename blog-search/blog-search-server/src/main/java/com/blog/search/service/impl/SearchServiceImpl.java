package com.blog.search.service.impl;

import com.blog.search.common.IndexKey;
import com.blog.search.model.PostDocument;
import com.blog.search.repository.PostRepository;
import com.blog.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 * Created by yuguidong on 2019/6/17.
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Page<PostDocument> query(Pageable pageable, String keyWord) {

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyWord,
                IndexKey.POST_TITLE,
                IndexKey.POST_DESCRIPTION,
                IndexKey.POST_AUTHOR,
                IndexKey.POST_CATEGORY,
                IndexKey.POST_TAGS);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withPageable(pageable)
                .build();

        Page<PostDocument> postDocuments = postRepository.search(searchQuery);

        log.info("查询 - {} - 的得到结果如下-------------> {}个查询结果，一共{}页",
                keyWord, postDocuments.getTotalElements(), postDocuments.getTotalPages());

        return postDocuments;
    }
}
