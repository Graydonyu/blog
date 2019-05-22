package com.blog.service;

import com.blog.entity.Post;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
public interface PostService extends BaseService<Post> {

    /**
     * 初始化首页的周评论排行榜
     */
    void initIndexWeekRank();

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    void zUnionAndStoreLast7DaysForLastWeekRank();

    /**
     * 给set里的文章评论加1，并且重新union7天的评论数量
     * @param postId
     */
    void incrZsetValueAndUnionForLastWeekRank(Long postId);
}
