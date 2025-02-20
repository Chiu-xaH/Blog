package com.chiuxah.blog.model.entity.collection

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Timestamp

// TABLE user_collection
data class CollectionEntity(
    override val id: Int = 0,
    val uid : Int,
    val article_id : Int,
    val folder_id : Int?,
    override val create_time: Timestamp? = null,
) : BaseSqlEntity()

