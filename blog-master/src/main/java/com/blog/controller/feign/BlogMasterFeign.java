package com.blog.controller.feign;

import com.baomidou.mybatisplus.extension.api.R;
import com.blog.search.dto.PostDTO;
import com.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blogMasterFeign")
public class BlogMasterFeign {

    @Autowired
    PostService postService;

    @GetMapping("/post/findPostDTOByPostId")
    public R<PostDTO> findPostDTOByPostId(long postId) {

        PostDTO postDTO = postService.findPostDTOById(postId);

        return R.ok(postDTO);
    }
}
