package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.entity.Post;
import com.blog.mapper.PostMapper;
import com.blog.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void join(Map<String, Object> map, String field) {
        if(CollectionUtil.isEmpty(map) || map.get(field) == null){
            return;
        }

        Map<String, Object> joinColumns = new HashMap<>();

        //字段的值
        String linkfieldValue = map.get(field).toString();

        Post post = this.getById(linkfieldValue);

        joinColumns.put("id", post.getId());
        joinColumns.put("title", post.getTitle());
        joinColumns.put("created", DateUtil.formatDate(post.getCreated()));

        map.put("post", joinColumns);
    }

    /**
     * 初始化首页的周评论排行榜
     */
    @Override
    public void initIndexWeekRank() {
        //缓存最近7天的文章评论数量
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                .ge("created", DateUtil.offsetDay(new Date(), -7).toJdkDate())
                .select("id, title, user_id, comment_count, view_count, created"));

        for (Post post : last7DayPosts) {
            String key = "day_rank:" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);

            //设置有效期
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章到set中，评论数量作为排行标准
            redisUtil.zSet(key, post.getId(), post.getCommentCount());
            //设置有效期
            redisUtil.expire(key, expireTime);

            //缓存文章基本信息（hash结构）
            this.hashCachePostIdAndTitle(post);
        }

        //7天阅读相加。
        this.zUnionAndStoreLast7DaysForLastWeekRank();
    }

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    @Override
    public void zUnionAndStoreLast7DaysForLastWeekRank() {
        String prifix = "day_rank:";

        List<String> keys  = new ArrayList<>();
        String key = prifix + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);

        for(int i = -7 ; i < 0; i++) {
            Date date = DateUtil.offsetDay(new Date(), i).toJdkDate();
            keys.add(prifix + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        }

        redisUtil.zUnionAndStore(key, keys, "last_week_rank");
    }

    /**
     * 给set里的文章评论加1，并且重新union7天的评论数量
     * @param postId
     */
    @Override
    public void incrZsetValueAndUnionForLastWeekRank(Long postId) {
        String dayRank = "day_rank:" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        //文章阅读加一
        redisUtil.zIncrementScore(dayRank, postId, 1);

        this.hashCachePostIdAndTitle(this.getById(postId));

        //重新union最近7天
        this.zUnionAndStoreLast7DaysForLastWeekRank();
    }

    /**
     * hash结构缓存文章标题和id
     * @param post
     */
    private void hashCachePostIdAndTitle(Post post) {

        boolean isExist = redisUtil.hasKey("rank_post_" + post.getId());
        if(!isExist) {
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章基本信息（hash结构）
            redisUtil.hset("rank_post_" + post.getId(), "post:id", post.getId(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
            //redisUtil.hset("rank_post_" + post.getId(), "post:comment_count", post.getCommentCount(), expireTime);
        }
    }
}
