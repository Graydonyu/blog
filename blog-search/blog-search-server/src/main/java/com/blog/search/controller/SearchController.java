package com.blog.search.controller;

import com.blog.common.resultVO.R;
import com.blog.search.model.PostDocument;
import com.blog.search.service.SearchService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yuguidong on 2019/6/14.
 */
@Api("es搜索Controller")
@RestController
@RequestMapping("/blog-search")
public class SearchController {

    @Autowired
    SearchService searchService;

    @GetMapping("/search")
    public R search(@RequestParam Integer page,
                    @RequestParam Integer size,
                    @RequestParam String keyWord){
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDocument> postDocuments = searchService.query(pageable,keyWord);

        return R.ok(postDocuments);
    }
}
