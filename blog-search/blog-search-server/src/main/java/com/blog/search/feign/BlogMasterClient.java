package com.blog.search.feign;

import com.blog.common.resultVO.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "blog-master")
public interface BlogMasterClient {

    @GetMapping("/blogMasterFeign/post/findPostDTOByPostId")
    R findPostDTOByPostId(@RequestParam("postId") long postId);
}
