package com.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Post;
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
@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    @GetMapping("/{id}")
    public String category(@PathVariable Long id,
                           @RequestParam(defaultValue = "1") Integer current,
                           @RequestParam(defaultValue = "10")Integer size) {

        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, new QueryWrapper<Post>().eq("category_id", id).orderByDesc("created"));

        //添加关联的用户信息
        userService.join(pageData, "user_id");
        categoryService.join(pageData, "category_id");

        req.setAttribute("pageData", pageData);
        req.setAttribute("currentCategoryId", id);
        return "post/category";
    }
}

