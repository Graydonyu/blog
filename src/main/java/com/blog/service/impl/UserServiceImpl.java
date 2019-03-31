package com.blog.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
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

        if (CollectionUtils.isNotEmpty(records)) {
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

    @Override
    public R register(User user) {
        if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword())
                || StringUtils.isEmpty(user.getUsername())) {
            return R.failed("必要字段不能为空");
        }

        User po = this.getOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        if(po != null) {
            return R.failed("邮箱已被注册");
        }

        String passMd5 = SecureUtil.md5(user.getPassword());

        po = new User();
        po.setEmail(user.getEmail());
        po.setPassword(passMd5);
        po.setCreated(new Date());
        po.setUsername(user.getUsername());
        po.setAvatar("/images/avatar/default.png");
        po.setPoint(0);

        return this.save(po)? R.ok("") : R.failed("注册失败");
    }
}
