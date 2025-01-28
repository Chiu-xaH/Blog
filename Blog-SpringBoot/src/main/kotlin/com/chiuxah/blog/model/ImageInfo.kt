package com.chiuxah.blog.model

import java.sql.Timestamp

data class ImageInfo(
    val id : Int = 0,
    val url : String,
    val filename : String,
    val size : Long,
    val filetype : String,
    val uploadtime : Timestamp? = null,
    val uid : Int,
    val type : Int,
    val state : Int
)
