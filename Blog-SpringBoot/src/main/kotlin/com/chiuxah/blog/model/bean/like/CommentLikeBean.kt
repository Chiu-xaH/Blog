package com.chiuxah.blog.model.bean.like

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

data class CommentLikeBean(
    override val id: Int = 0,
    override val create_time: Timestamp? = null,
    val uid : Int,
    val comment_id : Int
) : BaseSqlBean()
