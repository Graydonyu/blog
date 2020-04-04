package com.blog.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.blog.entity.User;
import com.blog.shiro.AccountProfile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
public interface IUserService extends IBaseService<User> {

    /**
     *@Author ：yuguidong
     *@Date ：2019/3/22
     *@Discription ：注册用户信息
     */
    R register(User user);

    /**
     *@Author ：yuguidong
     *@Date ：2019/4/19
     *@Discription ：登录认证
     */
    AccountProfile login(String email, String password);
}
