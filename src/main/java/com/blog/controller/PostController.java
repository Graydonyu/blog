package com.blog.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Slf4j
@Controller
@RequestMapping("/post")
public class PostController extends BaseController{

    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id){

        Map<String, Object> post = postService.getMap(new QueryWrapper<Post>().eq("id", id));

        if(CollectionUtil.isNotEmpty(post)){
            userService.join(post,"user_id");
        }

        Assert.notNull(post, "该文章已被删除");

        req.setAttribute("post", post);
        return "post";
    }
}

