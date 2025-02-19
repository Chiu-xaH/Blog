package com.chiuxah.blog.config.componment.rabbitmq

import com.chiuxah.blog.service.MailService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

// 消费者
@Component
class Consumer {
    @Autowired
    lateinit var mailService : MailService
    @RabbitListener(queues = [RabbitMQConfig.MAIL_QUEUE_NAME])
    fun processEmailMsg(message: Map<String, String>) {
        val email = message["email"]
        val code = message["code"]

        if (email != null && code != null) {
            mailService.send(email, code)
        }
    }
}


