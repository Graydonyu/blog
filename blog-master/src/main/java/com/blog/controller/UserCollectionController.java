package com.blog.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.blog.entity.Post;
import com.blog.entity.UserCollection;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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

    /**
     * 收藏
     * @param postId
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public R collectionAdd(Long postId) {

        Post post = postService.getById(postId);

        Assert.isTrue(post != null, "该帖子已被删除");

        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("post_id", postId)
                .eq("user_id", getProfileId()));

        if(count > 0) {
            return R.failed("你已经收藏");
        }

        UserCollection collection = new UserCollection();
        collection.setUserId(getProfileId());
        collection.setCreated(new Date());
        collection.setModified(new Date());

        collection.setPostId(post.getId());
        collection.setPostUserId(post.getUserId());

        userCollectionService.save(collection);

        return R.ok(MapUtil.of("collection", true));
    }

    /**
     * 取消收藏
     * @param postId
     * @return
     */
    @ResponseBody
    @PostMapping("/remove")
    public R collectionRemove(String postId) {

        Post post = postService.getById(Long.valueOf(postId));

        Assert.isTrue(post != null, "该帖子已被删除");

        boolean hasRemove = userCollectionService.remove(new QueryWrapper<UserCollection>()
                .eq("post_id", postId)
                .eq("user_id", getProfileId()));

        return R.ok(hasRemove);
    }
}

