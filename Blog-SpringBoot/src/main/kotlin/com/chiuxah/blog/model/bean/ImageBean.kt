package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp
// TABLE image_info
data class ImageBean(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val url : String,
    val filename : String,
    val size : Long,
    val filetype : String,
    val uid : Int,
    val type : Int,
) : BaseSqlBean()
