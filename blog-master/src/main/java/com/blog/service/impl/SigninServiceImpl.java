package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Signin;
import com.blog.entity.User;
import com.blog.factory.PointsSimpleFactory;
import com.blog.mapper.SigninMapper;
import com.blog.service.ISigninService;
import com.blog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SigninServiceImpl extends ServiceImpl<SigninMapper, Signin> implements ISigninService {

    @Override
    public Map<String, Object> status() {
        AccountProfile accountProfile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if(accountProfile == null){
            return null;
        }

        //查询今日是否签到
        boolean signed = false;
        //积分
        int points;
        //连续登陆天数
        int days = accountProfile.getContinuousSigninDays();

        Map<String,Object> resultMap = new HashMap<>();

        Signin signin = getOne(new QueryWrapper<Signin>()
                .eq("user_id",accountProfile.getId())
                .eq("signin_date",new java.sql.Date(new Date().getTime())));

        if(signin == null){
            points = PointsSimpleFactory.getPointsBySigninDays(days + 1);
        }else{
            signed = true;
            points = signin.getAccumulatePoints();
        }

        resultMap.put("days", days);
        resultMap.put("signed", signed);
        resultMap.put("points", points);
        resultMap.put("userId", accountProfile.getId());
        return resultMap;
    }

    @Override
    public Map<String, Object> signin(Signin signin) {
        //签到者与登陆态不对应
        AccountProfile accountProfile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if(accountProfile == null || !signin.getUserId().equals(accountProfile.getId())){
            return null;
        }

        //连续登陆天数
        int days = accountProfile.getContinuousSigninDays() + 1;
        //积分
        int points = PointsSimpleFactory.getPointsBySigninDays(days);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("signed", true);

        signin.setAccumulatePoints(points);
        signin.setSigninDate(new Date());
        signin.setSigninTime(new Date());
        signin.setCreated(new Date());
        signin.setModified(new Date());

        /*User user = new User();
        user.setPoint(accountProfile.getPoint() + points);
        user.setLastSigninDay(new Date());
        user.setModified(new Date());
        user.setContinuousSigninDays(days);
        if(){

        }*/

        save(signin);

        resultMap.put("days", days);
        resultMap.put("points", points);
        return resultMap;
    }
}
