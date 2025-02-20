package com.chiuxah.blog.model.entity

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Timestamp

data class ReadHistoryEntity(
    override val id: Int = 0,
    override val create_time: Timestamp? = null,
    val uid : Int,
    val article_id : Int,
    val last_view_time : Timestamp
) : BaseSqlEntity()