package com.blog.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Category;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.enums.IsEnum;
import com.blog.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
@RequestMapping("/post")
public class PostController extends BaseController{

    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id,
                                @RequestParam(defaultValue = "1") Integer current,
                                @RequestParam(defaultValue = "10")Integer size){

        Map<String, Object> post = postService.getMap(new QueryWrapper<Post>().eq("id", id));

        userService.join(post, "user_id");
        categoryService.join(post, "category_id");

        Assert.notNull(post, "该文章已被删除");

        postService.setViewCount(post, IsEnum.YES);

        req.setAttribute("post", post);
        req.setAttribute("currentCategoryId", post.get("category_id"));


        Page<Comment> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = commentService.pageMaps(page, new QueryWrapper<Comment>()
                .eq("post_id", id)
                .orderByDesc("created"));

        userService.join(pageData, "user_id");
        commentService.join(pageData, "parent_id");

        req.setAttribute("pageData", pageData);

        return "post/post";
    }

    @GetMapping("/execute/add")
    public String getPost() {

        String id = req.getParameter("id");
        Post post = new Post();
        if(!StringUtils.isEmpty(id)) {
            post = postService.getById(Long.valueOf(id));
        }

        req.setAttribute("pid", id);
        req.setAttribute("post", post);

        List<Category> categories = categoryService.list(new QueryWrapper<Category>()
                .orderByDesc("order_num"));

        req.setAttribute("categories", categories);

        return "post/add";
    }

    @ResponseBody
    @PostMapping("/execute/publishPost")
    public R publishPost(@Valid Post post, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return R.failed(bindingResult.getFieldError().getDefaultMessage());
        }

        if(post.getId() == null) {
            post.setUserId(getProfileId());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(Constant.EDIT_HTML_MODEL);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            post.setStatus(Constant.NORMAL_STATUS);
        } else {

            Post tempPost = postService.getById(post.getId());
            if(tempPost.getUserId() != getProfileId()) {
                return R.failed("不是自己的帖子");
            }
        }

        postService.saveOrUpdate(post);

        //TODO 给所有订阅的人发送消息

        return R.ok(post.getId());
    }

    @ResponseBody
    @Transactional
    @PostMapping("/execute/delete")
    public R postDelete(Long id) {
        Post post = postService.getById(id);

        Assert.isTrue(post != null, "该帖子已被删除");

        Long profileId = getProfileId();
        if(post.getUserId() != profileId) {
            return R.failed("不能删除非自己的帖子");
        }

        postService.removeById(id);

        // 同时删除所有的相关收藏
        userCollectionService.removeByMap(MapUtil.of("post_id", id));

        return R.ok(null);
    }

    /**
     * 思路：
     * 项目启动初始化最近7天发表文章的评论数量，
     * 发表评论给对应点的文章添加comment-count。同时设置有效期。
     *
     * 命令使用：
     * 1、ZINCRBY rank:20181020 5 1
     * 2、ZRANGE rank:20181020 0 -1 withscores
     * 3、ZREVRANGE rank:20181220 0 -1 withsroces
     * 4、ZINCRBY rank:20181019 99 1
     * 统计
     * 5、ZUNIONSTORE rank:last_week 7 rank:20181019 rank:20181020 rank:20181021 weights 1 1 1
     * 6、ZREVRANGE rank:last_week 0 -1 withscores
     * 设定有效期
     * 7、ttl rank:last_week
     * 8、expire rank:last_week 60*60*24*7
     * @return
     */
    @ResponseBody
    @GetMapping("/hots")
    public R hotPost() {

        Set<ZSetOperations.TypedTuple> lastWeekRank = redisUtil.getZSetRank("last_week_rank", 0, 6);

        List<Map<String, Object>> hotPosts = new ArrayList<>();
        for (ZSetOperations.TypedTuple typedTuple : lastWeekRank) {

            Map<String, Object> map = new HashMap<>();
            map.put("comment_count", typedTuple.getScore());
            map.put("id", redisUtil.hget("rank_post_" + typedTuple.getValue(), "post:id"));
            map.put("title", redisUtil.hget("rank_post_" + typedTuple.getValue(), "post:title"));

            hotPosts.add(map);
        }

        return R.ok(hotPosts);
    }
}

