package com.chiuxah.blog.config.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer

//@Configuration
class RedisConfig {
//    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory

        // 创建JSON序列化工具
        val jsonRedisSerializer = GenericJackson2JsonRedisSerializer()
        // 设置Key的序列化
        template.keySerializer = RedisSerializer.string()
        template.hashKeySerializer = RedisSerializer.string()
        // 设置Value的序列化
        template.valueSerializer = jsonRedisSerializer
        template.hashValueSerializer = jsonRedisSerializer

        return template
    }
}