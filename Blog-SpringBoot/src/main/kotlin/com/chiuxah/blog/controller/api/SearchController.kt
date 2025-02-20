package com.chiuxah.blog.controller.api

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 搜索模块 负责各种类型的检索（不同于精确匹配），以及首页推荐 ES
@RestController
@RequestMapping("/api/v1/search")
class SearchController {
}