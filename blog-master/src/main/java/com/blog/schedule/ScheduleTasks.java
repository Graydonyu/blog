package com.blog.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.entity.Post;
import com.blog.service.PostService;
import com.blog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
@EnableScheduling
public class ScheduleTasks {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PostService postService;

    /**
     * 阅读数量同步任务
     * 每天2点同步
     */
    @Scheduled(cron = "0 0 2 * * ?")
    //@Scheduled(cron = "0 0/1 * * * *")//一分钟（测试用）
    public void postViewCountSync() {
        log.info("---------->进入定时任务同步阅读量缓存方法");
        Set<String> keys = redisUtil.scan("rank_post_");

        List<String> ids = new ArrayList<>();
        for (String key : keys) {
            String postId = key.substring("rank_post_".length());

            if(redisUtil.hHasKey("rank_post_" + postId, "post:viewCount")){
                ids.add(postId);
            } else {
                keys.remove("rank_post_" + postId);
            }
        }

        if(ids.isEmpty()) return;

        List<Post> posts = postService.list(new QueryWrapper<Post>().in("id", ids));

        Iterator<Post> it = posts.iterator();
        while (it.hasNext()) {
            Post post = it.next();
            Object count =redisUtil.hget("rank_post_" + post.getId(), "post:viewCount");
            if(count != null) {
                post.setViewCount(Integer.valueOf(count.toString()));
            } else {
                //不需要同步的
                it.remove();
                keys.remove("rank_post_" + post.getId());
            }
        }

        if(posts.isEmpty()) return;

        boolean isSuccess = postService.updateBatchById(posts);
        if(isSuccess) {
            for(Post post : posts) {
                // 删除缓存中的阅读数量，防止重复同步（根据实际情况来）
                redisUtil.hdel("rank_post_" + post.getId(), "post:viewCount");
            }
        }

        log.info("同步文章阅读成功 ------> {}", keys);
    }
}
