package com.chiuxah.blog.config

import com.chiuxah.blog.config.interception.LoginInterception
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry

// 配置系统文件
@Configuration
class ApplicationConfig : WebMvcConfigurer {
    @Autowired
    lateinit var loginInterception : LoginInterception
    // 装配拦截器
    @Value("\${file.upload-dir}") // 保存路径
    lateinit var uploadDir: String

    @Value("\${file.location-url}") // 映射路径
    lateinit var locationUrl: String

    companion object {
        const val API = "/api/v1"
        const val ARTICLE = "/article"
        const val USER = "/user"
        const val FOLLOW = "/follow"
        const val COLLECTION = "/collection"
        const val COMMENT = "/comment"
        const val LIKE = "/like"
    }
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterception).addPathPatterns("${API}/**") // 全部拦截
            // 除外 不拦截
            .apply {
                excludePathPatterns("$API$ARTICLE/all")
                excludePathPatterns("$API$ARTICLE/info")
                excludePathPatterns("$API$ARTICLE/user")

                excludePathPatterns("$API$USER/login")
                excludePathPatterns("$API$USER/reg")
                excludePathPatterns("$API$USER/info")

                excludePathPatterns("${API}/upload/image/**")

                excludePathPatterns("$API$FOLLOW/count")
                excludePathPatterns("$API$LIKE$ARTICLE/count")
                excludePathPatterns("$API$LIKE$COMMENT/count")

                excludePathPatterns("$API$COMMENT/info")
                excludePathPatterns("$API$COMMENT/all")
                excludePathPatterns("$API$COMMENT/count")
            }
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("$locationUrl**")
            .addResourceLocations("file:${uploadDir}/")
    }
}