package com.chiuxah.blog.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry

// 配置系统文件
@Configuration
class ApplicationConfig : WebMvcConfigurer {
    @Autowired lateinit var loginInterception : LoginInterception
    // 装配拦截器
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterception)
            .addPathPatterns("/**") // 全部拦截
            .excludePathPatterns("/article/get_list") // 除外 不拦截
            .excludePathPatterns("/article/get_by_blog_id") // 除外 不拦截
            .excludePathPatterns("/article/get_user_list") // 除外 不拦截
            .excludePathPatterns("/user/login") // 除外 不拦截
            .excludePathPatterns("/user/reg") // 除外 不拦截
            .excludePathPatterns("/uploads/images/**")
    }
}