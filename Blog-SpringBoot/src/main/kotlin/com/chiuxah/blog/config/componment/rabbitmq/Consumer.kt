package com.chiuxah.blog.config.componment.rabbitmq

import com.chiuxah.blog.config.componment.rabbitmq.config.RabbitMQConfigFanout
import com.chiuxah.blog.config.componment.rabbitmq.config.RabbitMqConfigDirect
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component


// 消费者
//@Component
class Consumer {
//    @RabbitListener(queues = [RabbitMQConfig.NAME_QUEUE])
    fun process1(message: String) {
        println("消费者1接受到消息=====$message")
    }

//    @RabbitListener(queues = [RabbitMQConfig.NAME_QUEUE])
    fun process2(message: String) {
        println("消费者2接受到消息=====$message")
    }

//    @RabbitListener(queues = [RabbitMQConfigFanout.QUEUE_1])
    fun receiveQueue1Fanout(msg : String) {
        println("消费者1接受到QUEUE1消息=====$msg")
    }
//    @RabbitListener(queues = [RabbitMQConfigFanout.QUEUE_2])
    fun receiveQueue2Fanout(msg : String) {
        println("消费者2接受到QUEUE2消息=====$msg")
    }

//    @RabbitListener(queues = [RabbitMqConfigDirect.QUEUE_DIRECT_1])
    fun receiveQueue1Direct(msg : String) {
        println("消费者1接受到QUEUE1消息=====$msg")
    }
//    @RabbitListener(queues = [RabbitMqConfigDirect.QUEUE_DIRECT_2])
    fun receiveQueue2Direct(msg : String) {
        println("消费者2接受到QUEUE2消息=====$msg")
    }
}


