package com.blog.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ygd
 * @since 2018-09-21
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/index")
    public String index() {
        log.info("-------------------------------->这是首页");
        return "index";
    }

}

