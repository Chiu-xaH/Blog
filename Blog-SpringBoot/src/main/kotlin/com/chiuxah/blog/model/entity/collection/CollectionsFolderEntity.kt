package com.chiuxah.blog.model.entity.collection

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Timestamp

// TABLE collection_category
data class CollectionsFolderEntity(
    override val id: Int = 0,
    val uid : Int,
    val name : String,
    val description : String?,
    override val create_time: Timestamp? = null,
    val state : Int
) : BaseSqlEntity()

