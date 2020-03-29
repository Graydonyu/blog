package com.blog.search.feign;

import com.blog.common.resultVO.R;
import com.blog.search.dto.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "blog-master")
public interface BlogMasterClient {

    @GetMapping("/blogSearchFeign/post/findPostDTOByPostId")
    R findPostDTOByPostId(@RequestParam("postId") long postId);

    @PostMapping("/blogSearchFeign/post/findPostDTOByPage")
    R<List<PostDTO>> findPostDTOByPage(@RequestParam("current") int current, @RequestParam("size") int size);
}
