package com.chiuxah.blog

import com.chiuxah.blog.config.componment.rabbitmq.Producer
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.Random

@SpringBootTest
class Tests {
    @Autowired
    lateinit var producer: Producer

    @Test
    @Throws(InterruptedException::class)
    fun contextLoads() = runBlocking {
        for (i in 0 until 5) {
//            producer.produceDirect1((i + 1).toString())
            producer.produceDirect2((i).toString())
            delay(Random().nextInt(100, 1000).toLong())  // 代替 Thread.sleep，避免阻塞线程
        }
    }
}