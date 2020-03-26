package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.Post;
import com.blog.search.dto.PostDTO;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ygd
 * @since 2019-03-22
 */
public interface PostMapper extends BaseMapper<Post> {

    PostDTO findPostDTOById(long postId);
}
