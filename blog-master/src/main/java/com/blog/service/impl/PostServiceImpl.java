package com.blog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.PraiseLog;
import com.blog.entity.enums.IsEnum;
import com.blog.entity.req.SetLevelOrRecommendReq;
import com.blog.mapper.PostMapper;
import com.blog.mapper.PraiseLogMapper;
import com.blog.search.dto.PostDTO;
import com.blog.service.ICategoryService;
import com.blog.service.ICommentService;
import com.blog.service.IPostService;
import com.blog.service.IUserService;
import com.blog.shiro.AccountProfile;
import com.blog.utils.Constant;
import com.blog.utils.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
@Service
@Transactional
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements IPostService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICategoryService categoryService;

    @Override
    public void join(Map<String, Object> map, String field) {
        if (CollectionUtil.isEmpty(map) || map.get(field) == null) {
            return;
        }

        Map<String, Object> joinColumns = new HashMap<>();

        //字段的值
        String linkfieldValue = map.get(field).toString();

        Post post = this.getById(linkfieldValue);

        joinColumns.put("id", post.getId());
        joinColumns.put("title", post.getTitle());
        joinColumns.put("created", DateUtil.formatDate(post.getCreated()));

        map.put("post", joinColumns);
    }

    /**
     * 初始化首页的周评论排行榜
     */
    @Override
    public void initIndexWeekRank() {
        //缓存最近7天的文章评论数量
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                .ge("created", DateUtil.offsetDay(new Date(), -7).toJdkDate())
                .select("id, title, user_id, comment_count, view_count, created"));

        for (Post post : last7DayPosts) {
            String key = "day_rank:" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);

            //设置有效期
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章到set中，评论数量作为排行标准
            redisUtil.zSet(key, post.getId(), post.getCommentCount());
            //设置有效期
            redisUtil.expire(key, expireTime);

            //缓存文章基本信息（hash结构）
            this.hashCachePostIdAndTitle(post);
        }

        //7天阅读相加。
        this.zUnionAndStoreLast7DaysForLastWeekRank();
    }

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    @Override
    public void zUnionAndStoreLast7DaysForLastWeekRank() {
        String prifix = "day_rank:";

        List<String> keys = new ArrayList<>();
        String key = prifix + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);

        for (int i = -7; i < 0; i++) {
            Date date = DateUtil.offsetDay(new Date(), i).toJdkDate();
            keys.add(prifix + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        }

        redisUtil.zUnionAndStore(key, keys, "last_week_rank");
    }

    /**
     * 给set里的文章评论加1，并且重新union7天的评论数量
     *
     * @param postId
     */
    @Override
    public void incrZsetValueAndUnionForLastWeekRank(Long postId) {
        String dayRank = "day_rank:" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        //文章阅读加一
        redisUtil.zIncrementScore(dayRank, postId, 1);

        this.hashCachePostIdAndTitle(this.getById(postId));

        //重新union最近7天
        this.zUnionAndStoreLast7DaysForLastWeekRank();
    }

    /**
     * hash结构缓存文章标题和id
     *
     * @param post
     */
    private void hashCachePostIdAndTitle(Post post) {

        boolean isExist = redisUtil.hHasKey("rank_post_" + post.getId(),"post:id");
        if (!isExist) {
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            //缓存文章基本信息（hash结构）
            redisUtil.hset("rank_post_" + post.getId(), "post:id", post.getId(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
            //redisUtil.hset("rank_post_" + post.getId(), "post:comment_count", post.getCommentCount(), expireTime);
        }
    }

    @Override
    public PostDTO findPostDTOById(long postId) {
        return postMapper.findPostDTOById(postId);
    }

    @Override
    public void setViewCount(Map<String, Object> postMap, IsEnum isEnum) {
        //获取redis中的阅读量
        Object viewCount = redisUtil.hget("rank_post_" + postMap.get("id"), "post:viewCount");

        if(viewCount == null){
            if(IsEnum.YES == isEnum){
                postMap.put("view_count",Integer.valueOf(postMap.get("view_count").toString()) + 1);
            }
        }else{
            postMap.put("view_count",viewCount);
        }
    }

    @Override
    public IPage<PostDTO> findPostDTOByPage(Page<PostDTO> page, String keyword) {
        return postMapper.findPostDTOByPage(page, keyword);
    }

    /**
     * @param req
     * @param id
     * @param current
     * @param size
     * @Description 获取文章详情
     * @Date 2020-04-22 15:29
     * @Author Graydon
     */
    @Override
    public void selectPostDetail(HttpServletRequest req, Long id, Integer current, Integer size, AccountProfile profile) {
        Map<String, Object> post = getMap(new QueryWrapper<Post>().eq("id", id));

        userService.join(post, "user_id");
        categoryService.join(post, "category_id");

        Assert.notNull(post, "该文章已被删除");

        setViewCount(post, IsEnum.YES);

        req.setAttribute("post", post);
        req.setAttribute("currentCategoryId", post.get("category_id"));


        Page<Comment> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = commentService.pageMaps(page, new QueryWrapper<Comment>()
                .eq("post_id", id)
                .orderByDesc("status","created"));

        userService.join(pageData, "user_id");
        commentService.join(pageData, "parent_id");

        //点赞情况
        if(CollectionUtils.isNotEmpty(pageData.getRecords())){
            List<Long> commentIds;
            if(profile != null){
                commentIds = commentService.selectPraiseCommentIdsOfUser(profile.getId(), id);
            }else{
                commentIds = Collections.emptyList();
            }
            pageData.getRecords().forEach(c -> {
                if(profile != null && commentIds.contains(c.get("id"))){
                    c.put("isPraise",true);
                }else{
                    c.put("isPraise",false);
                }
            });
        }

        req.setAttribute("pageData", pageData);
    }

    /**
     * @param setLevelOrRecommendReq
     * @Description 设置置顶加精
     * @Date 2020-04-22 17:19
     * @Author Graydon
     */
    @Override
    public void setLevelOrRecommend(SetLevelOrRecommendReq setLevelOrRecommendReq) {
        Long id = setLevelOrRecommendReq.getId();
        String field = setLevelOrRecommendReq.getField();
        Integer rank = setLevelOrRecommendReq.getRank();
        Post post = getById(id);

        if("level".equals(field)){
            post.setLevel(rank);
        }else if("recommend".equals(field)){
            if(rank == 1){
                post.setRecommend(true);
            }else{
                post.setRecommend(false);
            }
        }

        updateById(post);
    }

    /**
     * @param comment
     * @Description 采纳
     * @Date 2020-04-23 16:46
     * @Author Graydon
     */
    @Override
    @Transactional
    public void accept(Comment comment) {
        Long commentId = comment.getId();
        comment = commentService.getById(commentId);

        Post post = getById(comment.getPostId());

        if (post.getStatus() == Constant.END_STATUS || post.getCommentId() != null) return;

        comment.setStatus(Constant.END_STATUS);

        post.setCommentId(commentId);
        post.setStatus(Constant.END_STATUS);

        updateById(post);
        commentService.updateById(comment);

        //TODO：下发积分操作
    }
}
