package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
public interface UserService extends IService<User> {

    /**
     *@Author ：yuguidong
     *@Date ：2019/3/22
     *@Discription ：添加关联的用户信息
     */
    void join(IPage<Map<String, Object>> pageData, String user_id);
}
