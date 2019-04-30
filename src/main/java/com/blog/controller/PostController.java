package com.blog.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.blog.entity.Category;
import com.blog.entity.Post;
import com.blog.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
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
            categoryService.join(post, "category_id");
        }

        Assert.notNull(post, "该文章已被删除");

        req.setAttribute("post", post);
        req.setAttribute("currentCategoryId", post.get("category_id"));
        return "post/post";
    }

    @GetMapping("/add")
    public String getPost() {

        String id = req.getParameter("id");
        Post post = new Post();
        if(!StringUtils.isEmpty(id)) {
            post = postService.getById(Long.valueOf(id));
        }

        req.setAttribute("pid", id);
        req.setAttribute("post", post);

        List<Category> categories = categoryService.list(new QueryWrapper<Category>()
                .orderByDesc("order_num"));

        req.setAttribute("categories", categories);

        return "post/add";
    }

    @ResponseBody
    @PostMapping("/publishPost")
    public R publishPost(@Valid Post post, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return R.failed(bindingResult.getFieldError().getDefaultMessage());
        }

        if(post.getId() == null) {
            post.setUserId(getProfileId());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(Constant.EDIT_HTML_MODEL);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            post.setStatus(Constant.NORMAL_STATUS);
        } else {

            Post tempPost = postService.getById(post.getId());
            if(tempPost.getUserId() != getProfileId()) {
                return R.failed("不是自己的帖子");
            }
        }

        postService.saveOrUpdate(post);

        //TODO 给所有订阅的人发送消息

        return R.ok(post.getId());
    }

}

