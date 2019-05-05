package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.blog.entity.Post;
import com.blog.mapper.PostMapper;
import com.blog.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements PostService {

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
}
