package com.chiuxah.blog.service

import com.chiuxah.blog.config.componment.rabbitmq.RabbitMQConfig
import com.chiuxah.blog.utils.CodeGeneratorUtil
import jakarta.annotation.Resource
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.time.Duration


@Component
class MailService {
    @Resource
    private lateinit var mailSender: JavaMailSenderImpl
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, String>

    @Value("\${app.name}")
    private lateinit var title: String

    @Value("\${spring.mail.username}")
    private lateinit var sender: String
    fun addQueue(email: String) {
        //生成随机验证码
        val code: String = CodeGeneratorUtil.generateCode()
        // 将验证码和邮件发送的请求放入RabbitMQ队列
        val message = mapOf("email" to email, "code" to code)
        rabbitTemplate.convertAndSend(RabbitMQConfig.MAIL_EXCHANGE_NAME, RabbitMQConfig.MAIL_ROUTING_KEY,message)
        println("加入队列")
        // 将验证码存入Redis缓存，设置过期时间
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5))
    }

    fun send(email: String, code: String) {
        val mimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)
        //设置一个html邮件信息
        helper.setText("""
            <div style="font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9;">
                <h2 style="color: #4CAF50; text-align: center;">${title} 验证码</h2>
                <p style="font-size: 16px; color: #333;">您好，</p>
                <p style="font-size: 16px; color: #333;">您的验证码为：</p>
                <p style="font-size: 22px; font-weight: bold; color: #ff5722; text-align: center;">$code</p>
                <p style="font-size: 14px; color: #666;">请在 5 分钟内使用该验证码完成验证，过期后需要重新获取。</p>
                <p style="font-size: 14px; color: #666;">如果您未请求此验证码，请忽略此邮件。</p>
                <hr style="border: none; border-top: 1px solid #ddd;">
                <p style="text-align: center; font-size: 12px; color: #999;">Chiu-xaH's Blog</p>
            </div>
        """.trimIndent(), true)
        helper.setSubject("$title 验证码")
        helper.setTo(email)
        helper.setFrom(sender)
        println("已发送")
        mailSender.send(mimeMessage)
    }

    fun checkCode(email: String, code: String) : Boolean {
        val cachedCode = redisTemplate.opsForValue().get(email)
        return cachedCode != null && cachedCode == code
    }
}
