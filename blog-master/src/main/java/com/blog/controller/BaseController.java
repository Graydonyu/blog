package com.blog.controller;

import com.blog.client.BlogSearchClient;
import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import com.blog.service.PostService;
import com.blog.service.UserCollectionService;
import com.blog.service.UserMessageService;
import com.blog.service.UserService;
import com.blog.shiro.AccountProfile;
import com.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

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
public class BaseController {
    @Autowired
    HttpServletRequest req;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserCollectionService userCollectionService;

    @Autowired
    UserMessageService userMessageService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BlogSearchClient blogSearchClient;

    protected AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getProfileId() {
        return getProfile().getId();
    }
}
