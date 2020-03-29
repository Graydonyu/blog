package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.entity.Post;
import com.blog.search.dto.PostDTO;
import org.apache.ibatis.annotations.Param;

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

    IPage<PostDTO> findPostDTOByPage(Page<PostDTO> page, @Param("keyword") String keyword);
}
