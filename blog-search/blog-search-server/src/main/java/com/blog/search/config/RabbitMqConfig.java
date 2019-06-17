package com.blog.search.config;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yuguidong on 2019/6/17.
 */
@Configuration
public class RabbitMqConfig {

    // 队列、交换机、绑定key名称
    public final static String ES_QUEUE = "es_queue";
    public final static String ES_EXCHANGE = "es_exchange";
    public final static String ES_BIND_KEY = "es_index_message";

    @Bean
    public Queue queue(){
        return new Queue(ES_QUEUE);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(ES_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeMessage(Queue queue,DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ES_BIND_KEY);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
