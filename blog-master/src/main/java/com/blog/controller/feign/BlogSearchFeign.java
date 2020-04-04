package com.blog.controller.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.search.dto.PostDTO;
import com.blog.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blogSearchFeign")
public class BlogSearchFeign {

    @Autowired
    IPostService postService;

    @GetMapping("/post/findPostDTOByPostId")
    public R<PostDTO> findPostDTOByPostId(long postId) {

        PostDTO postDTO = postService.findPostDTOById(postId);

        return R.ok(postDTO);
    }

    @PostMapping("/post/findPostDTOByPage")
    R<List<PostDTO>> findPostDTOByPage(@RequestParam("current") int current, @RequestParam("size") int size){

        Page<PostDTO> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<PostDTO> pageData = postService.findPostDTOByPage(page, null);

        return R.ok(pageData.getRecords());
    }
}
