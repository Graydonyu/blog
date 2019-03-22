package com.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void join(IPage<Map<String, Object>> pageData, String linkfield) {
        List<Map<String, Object>> records = pageData.getRecords();

        if(CollectionUtils.isNotEmpty(records)){
            records.forEach(map -> {
                String userId = map.get(linkfield).toString();
                User user = this.getById(userId);

                Map<String, Object> author = new HashMap<>();
                author.put("username", user.getUsername());
                author.put("avatar", user.getAvatar());
                author.put("id", user.getId());

                map.put("author", author);
            });
        }
    }
}
