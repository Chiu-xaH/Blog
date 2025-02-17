package com.chiuxah.blog.config.componment.rabbitmq.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//@Configuration
class RabbitMqConfigDirect {
    companion object {
        const val QUEUE_DIRECT_1 = "direct_queue_1"
        const val QUEUE_DIRECT_2 = "direct_queue_2"
        const val EXCHANGE_DIRECT = "exchange-direct"
        const val CONSUMER_1 = "consumer_1"
        const val CONSUMER_2 = "consumer_2"
    }

//    @Bean
    fun exchange() : Exchange =
        ExchangeBuilder.directExchange(EXCHANGE_DIRECT).durable(true).build()

//    @Bean
    fun queue1() : Queue = Queue(QUEUE_DIRECT_1,true)

//    @Bean
    fun queue2() : Queue = Queue(QUEUE_DIRECT_2,true)

//    @Bean
    fun bindingQueue1() : Binding =
        BindingBuilder.bind(queue1()).to(exchange()).with(CONSUMER_1).noargs()

//    @Bean
    fun bindingQueue2() : Binding =
        BindingBuilder.bind(queue2()).to(exchange()).with(CONSUMER_2).noargs()

}