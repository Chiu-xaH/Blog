package com.chiuxah.blog.model

import java.sql.Timestamp

data class ImageInfo(
    val id : Int = 0,
    val url : String,
    val filename : String,
    val size : Long,
    val type : String,
    val uploadtime : Timestamp,
    val state : Int
)
