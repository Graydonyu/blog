package com.blog.controller;

import com.blog.client.BlogSearchClient;
import com.blog.service.ICategoryService;
import com.blog.service.ICommentService;
import com.blog.service.IPostService;
import com.blog.service.ISigninService;
import com.blog.service.IUserCollectionService;
import com.blog.service.IUserMessageService;
import com.blog.service.IUserService;
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
    IUserService userService;

    @Autowired
    IPostService postService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    ICommentService commentService;

    @Autowired
    IUserCollectionService userCollectionService;

    @Autowired
    IUserMessageService userMessageService;

    @Autowired
    ISigninService signinService;

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
