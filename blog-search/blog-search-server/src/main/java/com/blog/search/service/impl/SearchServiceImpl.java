package com.blog.search.service.impl;

import cn.hutool.core.date.DateUtil;
import com.blog.common.resultVO.R;
import com.blog.search.common.IndexKey;
import com.blog.search.dto.PostDTO;
import com.blog.search.dto.PostMqIndexMessage;
import com.blog.search.model.PostDocument;
import com.blog.search.repository.PostRepository;
import com.blog.search.service.SearchService;
import com.blog.search.feign.BlogMasterClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by yuguidong on 2019/6/17.
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BlogMasterClient blogMasterClient;

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

    @Override
    public void createOrUpdateIndex(PostMqIndexMessage message) {
        long postId = message.getPostId();

        R r = blogMasterClient.findPostDTOByPostId(postId);

        log.info("r--------> {}",  r);

        Map<String, Object> data = (Map<String, Object>) r.getData();
        //手动转换一下日期格式
        data.put("created", DateUtil.parseDateTime(String.valueOf(data.get("created"))));
        PostDTO postDTO = modelMapper.map(data, PostDTO.class);

        if(PostMqIndexMessage.CREATE.equals(message.getType())) {
            if(postRepository.existsById(postId)) {
                this.removeIndex(message);
            }
        }

        PostDocument postDocument = new PostDocument();
        modelMapper.map(postDTO, postDocument);

        PostDocument saveDoc = postRepository.save(postDocument);

        log.info("es 索引更新成功！ --> {}" , saveDoc.toString());

    }

    @Override
    public void removeIndex(PostMqIndexMessage message) {
        long postId = message.getPostId();

        postRepository.deleteById(postId);

        log.info("es 索引删除成功！ --> {}" , message.toString());
    }
}
