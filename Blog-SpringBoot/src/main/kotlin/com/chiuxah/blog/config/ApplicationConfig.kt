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
        const val HISTORY = "/history"
        const val COMMENT = "/comment"
        const val LIKE = "/like"
        const val COUNT = "/count"
        const val INFO = "/info"
        const val ALL = "/all"
    }
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterception).addPathPatterns("${API}/**") // 全部拦截
            // 除外 不拦截
            .apply {
                excludePathPatterns("$API$ARTICLE$ALL")
                excludePathPatterns("$API$ARTICLE$INFO")
                excludePathPatterns("$API$ARTICLE/user")

                excludePathPatterns("$API$USER/login")
                excludePathPatterns("$API$USER/reg")
                excludePathPatterns("$API$USER$INFO")
                excludePathPatterns("$API$USER/send-code")
                excludePathPatterns("$API$USER/login-from-code")

                excludePathPatterns("${API}/upload/image/**")

                excludePathPatterns("$API$FOLLOW$COUNT")
                excludePathPatterns("$API$LIKE$ARTICLE$COUNT")
                excludePathPatterns("$API$LIKE$COMMENT$COUNT")

                excludePathPatterns("$API$COMMENT$INFO")
                excludePathPatterns("$API$COMMENT$ALL")
                excludePathPatterns("$API$COMMENT$COUNT")

                excludePathPatterns("$API$HISTORY$ARTICLE$COUNT")
            }
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("$locationUrl**")
            .addResourceLocations("file:${uploadDir}/")
    }
}