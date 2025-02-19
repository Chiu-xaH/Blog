package com.chiuxah.blog.config.componment.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Producer {
    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    fun produceEmailMsg(msg : String) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.MAIL_EXCHANGE_NAME, RabbitMQConfig.MAIL_ROUTING_KEY,msg)
    }
}