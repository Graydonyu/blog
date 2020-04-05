package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Signin;
import com.blog.entity.vo.SignTopVO;

import java.util.List;
import java.util.Map;

public interface ISigninService extends IService<Signin> {

    /**
     * @Author graydon
     * @Date 2020-04-04 21:30
     * @Description 查询今日签到状态
     **/
    Map<String, Object> status();

    /**
     * @Author graydon
     * @Date 2020-04-04 23:11
     * @Description 签到
     **/
    Map<String, Object> signin(Signin signin);

    /**
     * @Author graydon
     * @Date 2020-04-05 21:08
     * @Description top20排行榜
     **/
    List<List<SignTopVO>> top();
}
