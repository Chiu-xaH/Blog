package com.chiuxah.blog.config.componment.rabbitmq

import com.chiuxah.blog.config.componment.rabbitmq.config.RabbitMQConfig
import com.chiuxah.blog.config.componment.rabbitmq.config.RabbitMQConfigFanout
import com.chiuxah.blog.config.componment.rabbitmq.config.RabbitMqConfigDirect
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

//@Component
class Producer {
//    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    fun produce(msg : String) {
        println("生产者产生消息=====$msg")
        rabbitTemplate.convertAndSend("", RabbitMQConfig.NAME_QUEUE,msg)
    }
    fun produceFanout(msg : String) {
        println("生产者产生消息exchange-fanout=====$msg")
        rabbitTemplate.convertAndSend(
            RabbitMQConfigFanout.EXCHANGE_FANOUT,
            "",
            msg
        )
    }

    fun produceDirect1(msg : String) {
        println("生产者产生消息exchange-direct给1=====$msg")
        rabbitTemplate.convertAndSend(RabbitMqConfigDirect.EXCHANGE_DIRECT, RabbitMqConfigDirect.CONSUMER_1,msg)
//        println("生产者产生消息exchange-direct给2=====$msg")
//        rabbitTemplate.convertAndSend(RabbitMqConfigDirect.EXCHANGE_DIRECT, RabbitMqConfigDirect.CONSUMER_2,msg)
    }
    fun produceDirect2(msg : String) {
//        println("生产者产生消息exchange-direct给1=====$msg")
//        rabbitTemplate.convertAndSend(RabbitMqConfigDirect.EXCHANGE_DIRECT, RabbitMqConfigDirect.CONSUMER_1,msg)
        println("生产者产生消息exchange-direct给2=====$msg")
        rabbitTemplate.convertAndSend(RabbitMqConfigDirect.EXCHANGE_DIRECT, RabbitMqConfigDirect.CONSUMER_2,msg)
    }
}