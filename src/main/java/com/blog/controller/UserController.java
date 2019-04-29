package com.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Post;
import com.blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping("/user")
public class UserController extends BaseController {

    @GetMapping("/center")
    public String center(@RequestParam(defaultValue = "1") Integer current,
                         @RequestParam(defaultValue = "10")Integer size){
        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        log.info("-------------->进入个人中心");

        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().eq("user_id", getProfileId()).orderByDesc("created");

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, wrapper);
        req.setAttribute("pageData", pageData);

        return "user/center";
    }

    @GetMapping("/collection")
    public String collection( @RequestParam(defaultValue = "1") Integer current,
                              @RequestParam(defaultValue = "10")Integer size) {

        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        req.setAttribute("pageData", new Page());

        return "user/collection";
    }

    @RequestMapping("/{id}")
    public String home(@PathVariable Long id) {

        return "user/home";
    }

    @GetMapping("/setting")
    public String setting() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);

        req.setAttribute("user", user);
        return "user/setting";
    }

    @GetMapping("/message")
    public String message() {
        return "user/message";
    }
}

