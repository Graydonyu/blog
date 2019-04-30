package com.blog.service.impl;

import com.blog.entity.UserMessage;
import com.blog.mapper.UserMessageMapper;
import com.blog.service.UserMessageService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ygd
 * @since 2019-04-30
 */
@Service
public class UserMessageServiceImpl extends BaseServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

}
