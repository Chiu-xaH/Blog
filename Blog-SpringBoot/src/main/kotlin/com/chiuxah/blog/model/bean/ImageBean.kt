package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp
// TABLE image_info
data class ImageBean(
    override val id : Int = 0,
    val url : String,
    val filename : String,
    val size : Long,
    val filetype : String,
    override val create_time : Timestamp? = null,
    val uid : Int,
    val type : Int,
    val state : Int
) : BaseSqlBean()
