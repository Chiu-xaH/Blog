package com.chiuxah.blog.config.componment.rabbitmq

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMQConfig {
    companion object {
        const val MAIL_QUEUE_NAME = "mail.queue"
        const val MAIL_EXCHANGE_NAME = "mail.exchange"
        const val MAIL_ROUTING_KEY = "mail.routing-key"
    }

    @Bean
    fun messageConverter(): MessageConverter = Jackson2JsonMessageConverter()

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter()  // 设置消息转换器
        return rabbitTemplate
    }

    @Bean
    fun mailQueue() : Queue = Queue(MAIL_QUEUE_NAME,true)

    @Bean
    fun exchange() : Exchange =
        ExchangeBuilder.directExchange(MAIL_EXCHANGE_NAME).durable(true).build()

    @Bean
    fun bindingMailQueue() : Binding =
        BindingBuilder.bind(mailQueue()).to(exchange()).with(MAIL_ROUTING_KEY).noargs()
}