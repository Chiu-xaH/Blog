package com.chiuxah.blog.model.entity

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Timestamp

// TABLE article_info
data class ArticleEntity(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val title : String,
    val content : String,
    val update_time : Timestamp? = null,
    val uid : Int,
    val state : Int //  0 审核中 1 已发布 2 私人可见
) : BaseSqlEntity()


