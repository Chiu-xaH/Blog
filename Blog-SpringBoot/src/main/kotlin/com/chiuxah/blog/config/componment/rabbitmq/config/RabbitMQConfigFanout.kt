package com.chiuxah.blog.config.componment.rabbitmq.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// 广播模型 生产者通知所有消费者
//@Configuration
class RabbitMQConfigFanout {
    companion object {
        const val QUEUE_1 = "queue_1"
        const val QUEUE_2 = "queue_2"
        const val EXCHANGE_FANOUT = "exchange-fanout"
    }
//    @Bean
    fun exchange() : Exchange =
        ExchangeBuilder
            .fanoutExchange(EXCHANGE_FANOUT)
            .durable(true).build()

//    @Bean
    fun queue1() : Queue = Queue(QUEUE_1,true)

//    @Bean
    fun queue2() : Queue = Queue(QUEUE_2,true)

//    @Bean
    fun bindingQueue1() : Binding =
        BindingBuilder
            .bind(queue1())
            .to(exchange())
            .with("")
            .noargs()

//    @Bean
    fun bindingQueue2() : Binding =
        BindingBuilder
            .bind(queue2())
            .to(exchange())
            .with("")
            .noargs()
}