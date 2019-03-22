package com.blog.controller;

import com.blog.service.PostService;
import com.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
}
