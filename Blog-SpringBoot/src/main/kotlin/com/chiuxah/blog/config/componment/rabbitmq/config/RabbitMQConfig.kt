package com.chiuxah.blog.config.componment.rabbitmq.config

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 任务模型 共用一个队列
//@Configuration
class RabbitMQConfig {
    companion object {
        const val NAME_QUEUE = "queue_blog"
    }
//    @Bean
    fun queue(): Queue {
        return Queue(NAME_QUEUE,true)
    }
}
