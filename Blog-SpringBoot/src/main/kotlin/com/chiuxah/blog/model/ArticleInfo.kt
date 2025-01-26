package com.chiuxah.blog.model

data class ArticleInfo(
    val id : Long = 0L,
    val title : String,
    val content : String,
    val createtime : String = "",
    val updatetime : String = "",
    val uid : Long,
    val rcount : Int, // 阅读量
    val state : Int // 1 已发布 2 编辑中
)
