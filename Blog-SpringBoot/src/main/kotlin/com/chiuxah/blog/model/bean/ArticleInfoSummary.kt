package com.chiuxah.blog.model.bean

import java.sql.Timestamp

data class ArticleInfoSummary(
    val id : Int = 0,
    val title : String,
    val update_time : Timestamp? = null,
    val uid : Int,
)
