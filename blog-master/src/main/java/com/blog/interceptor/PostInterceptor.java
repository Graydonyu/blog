package com.blog.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.entity.Post;
import com.blog.service.PostService;
import com.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Slf4j
public class PostInterceptor implements HandlerInterceptor {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PostService postService;

    /**
     * 拦截器，增加帖子阅读量并放入redis
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("拦截url的链接----------->{}", request.getServletPath());

        //因为拦截的是/post/*，所以需要正则匹配对应的url
        String postId = request.getServletPath().substring("/post/".length());

        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        boolean isMatch = pattern.matcher(postId).matches();

        log.info("postId是否匹配成功 ----> {}", isMatch);

        if(!isMatch)return;

        //如果是翻页直接放行
        String current = request.getParameter("current");
        if(StringUtils.isNotBlank(current) && !current.equals("1"))return;

        //判断hashKey是否存在
        boolean isExist = redisUtil.hHasKey("rank_post_" + postId, "post:viewCount");

        //阅读数+1
        if(isExist){
            redisUtil.hincr("rank_post_" + postId,"post:viewCount",1);
        }else{
            Post post = postService.getOne(new QueryWrapper<Post>().eq("id", postId).select("view_count"));

            redisUtil.hincr("rank_post_" + postId,"post:viewCount",post.getViewCount() + 1);
        }

        //tips 数据库阅读与缓存的阅读用定时器来同步
    }
}
