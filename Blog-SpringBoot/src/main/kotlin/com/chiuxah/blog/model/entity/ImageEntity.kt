package com.chiuxah.blog.model.entity

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Timestamp
// TABLE image_info
data class ImageEntity(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val url : String,
    val filename : String,
    val size : Long,
    val filetype : String,
    val uid : Int,
    val type : Int,
) : BaseSqlEntity()
