package com.blog.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.blog.entity.Signin;
import com.blog.entity.vo.SignTopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Slf4j
@Controller
@RequestMapping("/sign")
public class SigninController extends BaseController{

    @ResponseBody
    @PostMapping("/in")
    public R signin(Signin signin){
        Map<String,Object> resultMap = signinService.signin(signin);
        if(resultMap == null) return R.failed("签到失败!请重新登陆后签到");
        return new R().setCode(ApiErrorCode.SUCCESS.getCode()).setData(resultMap).setMsg("签到成功!");
    }

    @ResponseBody
    @PostMapping("/status")
    public R status(){
        Map<String,Object> resultMap = signinService.status();
        if(resultMap == null){
           return R.ok("");
        }

        return R.ok(resultMap);
    }

    @ResponseBody
    @GetMapping("/top")
    public R top(){
        List<List<SignTopVO>> list = signinService.top();
        return R.ok(list);
    }
}

