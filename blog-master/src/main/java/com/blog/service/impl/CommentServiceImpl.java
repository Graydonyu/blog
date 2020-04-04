package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.ICommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Service
@Transactional
public class CommentServiceImpl extends BaseServiceImpl<CommentMapper, Comment> implements ICommentService {

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
}
