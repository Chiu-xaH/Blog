package com.chiuxah.blog.controller.api

import com.chiuxah.blog.utils.ControllerUtils.DEVELOPING_RESPONSE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 搜索模块 负责各种类型的检索（不同于精确匹配） ES
@RestController
@RequestMapping("/api/v1/search")
class SearchController {
    @GetMapping("/user")
    fun searchUsers(username : String) : Any {
        return DEVELOPING_RESPONSE
    }

    @GetMapping("/article")
    fun searchArticles(content : String) : Any {
        return DEVELOPING_RESPONSE
    }
}