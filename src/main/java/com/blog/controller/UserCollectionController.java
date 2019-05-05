package com.blog.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.blog.entity.UserCollection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ygd
 * @since 2019-04-30
 */
@Controller
@RequestMapping("/collection")
public class UserCollectionController extends BaseController {

    /**
     * 判断是否收藏
     * @param postId
     * @return
     */
    @ResponseBody
    @PostMapping("/find")
    public R collectionFind(Long postId) {
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("post_id", postId)
                .eq("user_id", getProfileId()));

        return R.ok(MapUtil.of("collection", count > 0));
    }
}

