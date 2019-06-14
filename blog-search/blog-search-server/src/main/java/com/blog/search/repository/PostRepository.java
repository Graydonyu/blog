package com.blog.search.repository;

import com.blog.search.model.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yuguidong on 2019/6/14.
 */
@Repository
public interface PostRepository extends ElasticsearchRepository<PostDocument,Long> {
}
