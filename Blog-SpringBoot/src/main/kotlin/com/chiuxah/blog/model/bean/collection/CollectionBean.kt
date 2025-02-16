package com.chiuxah.blog.model.bean.collection

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

// TABLE user_collection
data class CollectionBean(
    override val id: Int = 0,
    val uid : Int,
    val article_id : Int,
    val folder_id : Int?,
    override val create_time: Timestamp? = null,
) : BaseSqlBean()

data class CollectionSummaryInfo(
    val id: Int = 0,
    val article_id : Int,
    val folder_id : Int?
)