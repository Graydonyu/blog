package com.blog.service.impl;

import com.blog.entity.UserCollection;
import com.blog.mapper.UserCollectionMapper;
import com.blog.service.UserCollectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-04-30
 */
@Service
@Transactional
public class UserCollectionServiceImpl extends BaseServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

}
