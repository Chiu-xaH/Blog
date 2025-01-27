package com.chiuxah.blog.model

import java.sql.Timestamp

data class UserInfo(
    val id : Int,
    val username : String,
    var password : String,
    val photo : String,
    val createtime : Timestamp? = null,
    val state : Int
)
