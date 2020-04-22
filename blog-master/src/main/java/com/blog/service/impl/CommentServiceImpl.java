package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.entity.Comment;
import com.blog.entity.PraiseLog;
import com.blog.entity.req.CommentPraiseReq;
import com.blog.mapper.CommentMapper;
import com.blog.mapper.PraiseLogMapper;
import com.blog.service.ICommentService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private PraiseLogMapper praiseLogMapper;

    @Override
    public void join(Map<String, Object> map, String field) {
        if(CollectionUtil.isEmpty(map) || map.get(field) == null){
            return;
        }

        Map<String, Object> joinColumns = new HashMap<>();

        if(map.get(field) == null) {
            return;
        }
        //字段的值
        String linkfieldValue = map.get(field).toString();
        Comment comment = this.getById(linkfieldValue);

        joinColumns.put("id", comment.getId());
        joinColumns.put("content", comment.getContent());
        joinColumns.put("created", comment.getCreated());

        map.put("comment", joinColumns);
    }

    /**
     * @param commentPraiseReq
     * @param userId
     * @Description 评论点赞功能
     * @Date 2020-04-22 15:13
     * @Author Graydon
     */
    @Override
    @Transactional
    public void praise(CommentPraiseReq commentPraiseReq, Long userId) {
        Comment comment = getById(commentPraiseReq.getId());
        PraiseLog praiseLog = new PraiseLog();
        praiseLog.setUserId(userId);
        praiseLog.setCommentId(comment.getId());
        praiseLog.setPostId(comment.getPostId());

        if(commentPraiseReq.isOk()){
            comment.setVoteUp(comment.getVoteUp() - 1);
            praiseLogMapper.delete(new QueryWrapper<>(praiseLog));
        }else{
            comment.setVoteUp(comment.getVoteUp() + 1);
            praiseLog.setCreated(new Date());
            praiseLog.setModified(new Date());
            praiseLogMapper.insert(praiseLog);
        }

        updateById(comment);
    }

    /**
     * @param userId
     * @param postId
     * @Description 根据用户id和帖子id查询出被点赞的评论id
     * @Date 2020-04-22 15:19
     * @Author Graydon
     */
    @Override
    public List<Long> selectPraiseCommentIdsOfUser(Long userId, Long postId) {
        List<PraiseLog> praiseLogs = praiseLogMapper.selectList(new QueryWrapper<PraiseLog>().eq("user_id", userId).eq("post_id", postId));
        if(CollectionUtils.isEmpty(praiseLogs)){
            return Collections.emptyList();
        }
        List<Long> commentIds = praiseLogs.stream().map(p -> p.getCommentId()).collect(Collectors.toList());
        return commentIds;
    }
}
