package com.blog.controller;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @ResponseBody
    @PostMapping("/add")
    public R commentAdd(@Valid Comment comment, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return R.failed(bindingResult.getFieldError().getDefaultMessage());
        }

        Post post = postService.getById(comment.getPostId());
        Assert.isTrue(post != null, "该帖子已被删除");

        comment.setUserId(getProfileId());
        comment.setCreated(new Date());
        comment.setModified(new Date());
        comment.setStatus(Constant.NORMAL_STATUS);
        commentService.save(comment);

        //评论数加一
        post.setCommentCount(post.getCommentCount() + 1);
        postService.saveOrUpdate(post);

        //更新首页排版的评论数量
        postService.incrZsetValueAndUnionForLastWeekRank(comment.getPostId());

        // TODO 记录动作

        // TODO 通知作者

        return R.ok(null);
    }

    @ResponseBody
    @PostMapping("/delete")
    public R commentDel(Long id) {

        Comment comment = commentService.getById(id);

        Assert.isTrue(comment!= null, "该评论已被删除");

        if(comment.getUserId() != getProfileId()) {
            return R.failed("删除失败！");
        }

        commentService.removeById(id);

        return R.ok(null);
    }
}

