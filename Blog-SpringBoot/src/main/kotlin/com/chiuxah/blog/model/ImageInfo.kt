package com.chiuxah.blog.model

import com.chiuxah.blog.model.base.BaseSqlBean
import java.sql.Timestamp

data class ImageInfo(
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
