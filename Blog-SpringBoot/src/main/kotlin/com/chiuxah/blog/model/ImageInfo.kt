package com.chiuxah.blog.model

data class ImageInfo(
    val id : Long = 0L,
    val url : String,
    val filename : String,
    val size : Long,
    val type : String,
    val uploadtime : String = "",
    val state : Int
)
