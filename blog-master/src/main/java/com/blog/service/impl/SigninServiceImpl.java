package com.blog.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.entity.Signin;
import com.blog.entity.User;
import com.blog.entity.vo.SignTopVO;
import com.blog.factory.PointsSimpleFactory;
import com.blog.mapper.SigninMapper;
import com.blog.service.ISigninService;
import com.blog.service.IUserService;
import com.blog.shiro.AccountProfile;
import com.blog.utils.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SigninServiceImpl extends ServiceImpl<SigninMapper, Signin> implements ISigninService {

    @Autowired
    IUserService iUserService;

    @Override
    public Map<String, Object> status() {
        AccountProfile accountProfile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if (accountProfile == null) {
            return null;
        }

        //查询今日是否签到
        boolean signed = false;
        //积分
        int points;
        //连续登陆天数
        int days = accountProfile.getContinuousSigninDays();

        Map<String, Object> resultMap = new HashMap<>();

        Signin signin = getOne(new QueryWrapper<Signin>()
                .eq("user_id", accountProfile.getId())
                .eq("signin_date", new java.sql.Date(new Date().getTime())));

        if (signin == null) {
            points = PointsSimpleFactory.getPointsBySigninDays(days + 1);
        } else {
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
        if (accountProfile == null || !signin.getUserId().equals(accountProfile.getId())) {
            return null;
        }

        Date date = new Date();
        //连续登陆天数
        int days = accountProfile.getContinuousSigninDays() + 1;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("signed", true);

        User user = new User();
        user.setId(accountProfile.getId());
        user.setLastSigninDay(date);
        user.setModified(date);
        if (accountProfile.getFirstSigninDay() == null
                || DateUtil.between(accountProfile.getLastSigninDay(), date, DateUnit.DAY) > 1) { //两次签到时间差距超过一天
            user.setFirstSigninDay(date);
            accountProfile.setFirstSigninDay(date);
            //重置签到天数
            days = Constant.ONE_DAY;
        }

        //积分
        int points = PointsSimpleFactory.getPointsBySigninDays(days);

        user.setPoint(accountProfile.getPoint() + points);
        user.setContinuousSigninDays(days);

        //更新session
        accountProfile.setLastSigninDay(date);
        accountProfile.setContinuousSigninDays(days);
        accountProfile.setPoint(user.getPoint());

        signin.setAccumulatePoints(points);
        signin.setSigninDate(date);
        signin.setSigninTime(date);
        signin.setCreated(date);
        signin.setModified(date);

        save(signin);
        iUserService.updateById(user);

        resultMap.put("days", days);
        resultMap.put("points", points);
        return resultMap;
    }

    @Override
    public List<List<SignTopVO>> top() {
        List<List<SignTopVO>> resultList = new ArrayList<>();

        List<SignTopVO> newestSignTopVOList = new ArrayList<>();
        List<SignTopVO> fastSignTopVOList = new ArrayList<>();
        List<SignTopVO> totalSignTopVOList = new ArrayList<>();

        Date nowDate = new Date();
        Set<Long> userIdSet = new HashSet<>();

        //最新签到功能
        List<Signin> newestSigninList = list(new QueryWrapper<Signin>()
                .eq("signin_date", new java.sql.Date(nowDate.getTime()))
                .orderByDesc("signin_time")
                .last("limit 20"));
        if(CollectionUtils.isNotEmpty(newestSigninList)){
            setUserIdSetAndSignTopVOList(newestSignTopVOList,newestSigninList,userIdSet);
        }

        //今日最快签到功能
        List<Signin> fastSigninList = list(new QueryWrapper<Signin>()
                .eq("signin_date", new java.sql.Date(nowDate.getTime()))
                .orderByAsc("signin_time")
                .last("limit 20"));
        if(CollectionUtils.isNotEmpty(fastSigninList)){
            setUserIdSetAndSignTopVOList(fastSignTopVOList,fastSigninList,userIdSet);
        }

        //查询相关用户信息
        if(CollectionUtils.isNotEmpty(userIdSet)){
            Collection<User> users = iUserService.listByIds(userIdSet);
            Map<Long, User> userMap = users
                    .stream()
                    .collect(Collectors.toMap(p -> p.getId(), p -> p));

            if(CollectionUtils.isNotEmpty(newestSignTopVOList)){
                newestSignTopVOList.forEach(p -> setUserToSignTopVO(p,userMap));
            }

            if(CollectionUtils.isNotEmpty(fastSignTopVOList)){
                fastSignTopVOList.forEach(p -> setUserToSignTopVO(p,userMap));
            }
        }

        //总签到榜功能
        List<User> userList = iUserService.list(new QueryWrapper<User>()
                .ne("continuous_signin_days",0)
                .orderByDesc("continuous_signin_days")
                .last("limit 20")
        );
        if(CollectionUtils.isNotEmpty(userList)){
            userList.forEach(u -> {
                SignTopVO signTopVO = new SignTopVO();
                signTopVO.setUserId(u.getId());
                signTopVO.setTime(u.getLasted());
                signTopVO.setMsec(u.getLastSigninDay().getTime());
                signTopVO.setDays(u.getContinuousSigninDays());
                signTopVO.getUser().put("username",u.getUsername());
                signTopVO.getUser().put("avatar",u.getAvatar());
                totalSignTopVOList.add(signTopVO);
            });
        }

        resultList.add(newestSignTopVOList);
        resultList.add(fastSignTopVOList);
        resultList.add(totalSignTopVOList);
        return resultList;
    }

    /**
     * @Author graydon
     * @Date 2020-04-05 21:40
     * @Description 合并年月日和时分秒
     **/
    private Date mergeDate(Date date,Date time){
        Date result = time;
        SimpleDateFormat sdt = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        SimpleDateFormat sd = new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
        SimpleDateFormat st = new SimpleDateFormat(DatePattern.NORM_TIME_PATTERN);

        try {
            result = sdt.parse(sd.format(date) + " " + st.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @Author graydon
     * @Date 2020-04-05 22:06
     * @Description 加入用户信息
     **/
    private void setUserToSignTopVO(SignTopVO signTopVO,Map<Long, User> userMap){
        User user = userMap.get(signTopVO.getUserId());
        signTopVO.setDays(user.getContinuousSigninDays());
        signTopVO.getUser().put("username",user.getUsername());
        signTopVO.getUser().put("avatar",user.getAvatar());
    }

    /**
     * @Author graydon
     * @Date 2020-04-05 22:15
     * @Description 统计所有用户id，同时set SignTopVO属性值
     **/
    private void setUserIdSetAndSignTopVOList(List<SignTopVO> signTopVOList,List<Signin> signinList,Set<Long> userIdSet){
        userIdSet.addAll(
                signinList
                        .stream()
                        .map(p -> {
                            SignTopVO signTopVO = new SignTopVO();
                            signTopVO.setUserId(p.getUserId());
                            Date date = mergeDate(p.getSigninDate(),p.getSigninTime());
                            signTopVO.setTime(date);
                            signTopVO.setMsec(date.getTime());
                            signTopVOList.add(signTopVO);
                            return p.getUserId();
                        })
                        .collect(Collectors.toSet())
        );
    }
}
