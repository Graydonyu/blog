package com.blog.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

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
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list2.add(2);
        list2.add(3);
        list1.retainAll(list2);
        list1.forEach(System.out::println);

        return "index";
    }

}

