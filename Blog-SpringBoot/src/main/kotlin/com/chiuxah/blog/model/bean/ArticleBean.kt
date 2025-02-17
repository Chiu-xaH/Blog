package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

// TABLE article_info
data class ArticleBean(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val title : String,
    val content : String,
    val update_time : Timestamp? = null,
    val uid : Int,
    val state : Int //  0 审核中 1 已发布 2 私人可见
) : BaseSqlBean()


data class ArticleInfoSummary(
    val id : Int = 0,
    val title : String,
    val update_time : Timestamp? = null,
    val uid : Int,
)
