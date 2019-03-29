package com.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Post;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Api(tags = {"首页相关"},value = "首页")
@Slf4j
@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {

    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";

    @Autowired
    Producer producer;

    @ApiOperation("首页")
    @GetMapping("/")
    public String index() {
        Page<Post> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, null);

        //添加关联的用户信息
        userService.join(pageData, "user_id");


        req.setAttribute("pageData", pageData);

        log.info("--------------->" + pageData.getRecords());
        log.info("-------------------------------" + page.getPages());

        return "index";
    }

    @ApiOperation("验证码")
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();

        //生成验证码图片
        BufferedImage producerImage = producer.createImage(text);

        SecurityUtils.getSubject().getSession().setAttribute(KAPTCHA_SESSION_KEY,text);

        ServletOutputStream outputStream = response.getOutputStream();

        ImageIO.write(producerImage,"jpg",outputStream);
    }
}
