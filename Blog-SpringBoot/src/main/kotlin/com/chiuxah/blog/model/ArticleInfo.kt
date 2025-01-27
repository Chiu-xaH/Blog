package com.chiuxah.blog.model

import java.sql.Timestamp

data class ArticleInfo(
    val id : Int = 0,
    val title : String,
    val content : String,
    val createtime : Timestamp? = null,
    val updatetime : Timestamp? = null,
    val uid : Int,
    val rcount : Int, // 阅读量
    val state : Int // 1 已发布 2 编辑中
)
