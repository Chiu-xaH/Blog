package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

// TABLE article_info
data class ArticleBean(
    override val id : Int = 0,
    val title : String,
    val content : String,
    override val create_time : Timestamp? = null,
    val update_time : Timestamp? = null,
    val uid : Int,
    val rcount : Int, // 阅读量
    val state : Int // 1 已发布 2 编辑中
) : BaseSqlBean()


data class ArticleInfoSummary(
    val id : Int = 0,
    val title : String,
    val update_time : Timestamp? = null,
    val uid : Int,
    val rcount : Int,
)
