package com.blog.service.impl;

import com.blog.entity.UserMessage;
import com.blog.mapper.UserMessageMapper;
import com.blog.service.IUserMessageService;
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
public class UserMessageServiceImpl extends BaseServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

}
