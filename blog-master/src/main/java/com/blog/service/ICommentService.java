package com.blog.service;

import com.blog.entity.Comment;
import com.blog.entity.req.CommentPraiseReq;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
public interface ICommentService extends IBaseService<Comment> {

    /**
     * @Description 评论点赞功能
     * @Date 2020-04-22 15:13
     * @Author Graydon
     **/
    void praise(CommentPraiseReq commentPraiseReq,Long userId);

    /**
     * @Description 根据用户id和帖子id查询出被点赞的评论id
     * @Date 2020-04-22 15:19
     * @Author Graydon
     **/
    List<Long> selectPraiseCommentIdsOfUser(Long userId,Long postId);
}
