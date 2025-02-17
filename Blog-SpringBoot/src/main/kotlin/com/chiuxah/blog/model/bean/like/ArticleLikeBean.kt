package com.chiuxah.blog.model.bean.like

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

data class ArticleLikeBean(
    override val id: Int = 0,
    override val create_time: Timestamp? = null,
    val uid : Int,
    val article_id : Int
) : BaseSqlBean()


