package com.blog.config;

import com.blog.service.ICategoryService;
import com.blog.service.IPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Slf4j
@Order(1000)
@Component
public class ContextStartup implements ApplicationRunner, ServletContextAware {

    private ServletContext servletContext;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IPostService postService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        servletContext.setAttribute("categorys",categoryService.list(null));

        log.info("ContextStartup------------>加载categorys");

        //初始化首页的周评论排行榜
        postService.initIndexWeekRank();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
