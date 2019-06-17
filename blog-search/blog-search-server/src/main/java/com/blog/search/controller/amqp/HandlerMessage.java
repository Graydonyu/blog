package com.blog.search.controller.amqp;

import com.blog.search.config.RabbitMqConfig;
import com.blog.search.dto.PostMqIndexMessage;
import com.blog.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听异步消息队列
 * 更新搜索内容
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMqConfig.ES_QUEUE)
public class HandlerMessage {

    @Autowired
    SearchService searchService;

    @RabbitHandler
    public void handler(PostMqIndexMessage message) {
        switch (message.getType()) {
            case PostMqIndexMessage.CREATE:
            case PostMqIndexMessage.UPDATE:
                searchService.createOrUpdateIndex(message);
                break;
            case PostMqIndexMessage.REMOVE:
                searchService.removeIndex(message);
                break;
            default:
                log.warn("没有找到对应的消息类型，请注意！！！, --->  {}", message.toString());
                break;
        }
        /*try {


        } catch (IOException e) {
            log.error("这是内容----> {}", message.toString());
            log.error("处理HandlerMessage失败 --> ", e);
        }*/
    }
}
