package com.chiuxah.blog

import com.chiuxah.blog.model.UserInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate


@SpringBootTest
class BlogApplicationTests {
//	lateinit var jedis : Jedis
//
//	@BeforeEach
//	fun setUp() {
////		jedis = Jedis("127.0.0.1",6379)
//		jedis = JedisConnectionFactory.jedis
//		jedis.apply {
//			// 选择数据库 0-15
//			select(0)
//		}
//	}

//	@AfterEach
//	fun tearDown() = jedis.close()

//	@Autowired lateinit var redisTemplate: RedisTemplate<String,Any>
//	@Test
//	fun contextLoads() {
//		val result = redisTemplate.opsForValue().set("name","小明")
//		val name  = redisTemplate.opsForValue().get("name")
//		println(name)
//	}
	@Autowired lateinit var redisTemplate: StringRedisTemplate
	private val mapper = ObjectMapper()

	@Test
	fun test() {
		val user = UserInfo(1,"小明","zhaosihan0908","",null,1)
		val json = mapper.writeValueAsString(user)
		redisTemplate.opsForValue().set("user:1",json)
		val getValue = redisTemplate.opsForValue().get("user:1")
		println(getValue)
		val getUser = mapper.readValue(getValue,UserInfo::class.java)
		println(getUser)
	}
}


