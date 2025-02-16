package com.chiuxah.blog.model.bean.collection

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

// TABLE collection_category
data class CollectionsFolderBean(
    override val id: Int = 0,
    val uid : Int,
    val name : String,
    val description : String?,
    override val create_time: Timestamp? = null,
    val state : Int
) : BaseSqlBean()

