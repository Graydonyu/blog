package com.blog.client;

import com.baomidou.mybatisplus.extension.api.R;
import com.blog.search.dto.SearchResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "blog-search")
public interface BlogSearchClient {

    @RequestMapping("/blog-search/search")
    R<SearchResultDTO> search(@RequestParam("current") Integer current, @RequestParam("size") Integer size, @RequestParam("keyword") String keyword);
}
