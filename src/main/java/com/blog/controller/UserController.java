package com.blog.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/user")
public class UserController {

    @GetMapping("/center")
    public String center(){
        log.info("-------------->进入个人中心");

        return "user/home";
    }
}

