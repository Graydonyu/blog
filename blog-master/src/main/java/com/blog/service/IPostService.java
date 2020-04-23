package com.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.entity.enums.IsEnum;
import com.blog.entity.req.SetLevelOrRecommendReq;
import com.blog.search.dto.PostDTO;
import com.blog.shiro.AccountProfile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
public interface IPostService extends IBaseService<Post> {

    /**
     * 初始化首页的周评论排行榜
     */
    void initIndexWeekRank();

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    void zUnionAndStoreLast7DaysForLastWeekRank();

    /**
     * 给set里的文章评论加1，并且重新union7天的评论数量
     * @param postId
     */
    void incrZsetValueAndUnionForLastWeekRank(Long postId);

    /**
     * 根据id查询帖子，并交给搜索服务使用
     * @param postId
     */
    PostDTO findPostDTOById(long postId);

    /**
     * 显示redis中的文章阅读量
     * @param postMap
     * @param isEnum 判断是否需要在原有阅读量上+1
     */
    void setViewCount(Map<String, Object> postMap, IsEnum isEnum);

    /**
     * 分页查询所有帖子，提供给搜索服务
     * @param page
     */
    IPage<PostDTO> findPostDTOByPage(Page<PostDTO> page, String keyword);

    /**
     * @Description 获取文章详情
     * @Date 2020-04-22 15:29
     * @Author Graydon
     **/
    void selectPostDetail(HttpServletRequest req, Long id, Integer current, Integer size, AccountProfile profile);

    /**
     * @Description 设置置顶加精
     * @Date 2020-04-22 17:19
     * @Author Graydon
     **/
    void setLevelOrRecommend(SetLevelOrRecommendReq setLevelOrRecommendReq);

    /**
     * @Description 采纳
     * @Date 2020-04-23 16:46
     * @Author Graydon
     **/
    void accept(Comment comment);
}
