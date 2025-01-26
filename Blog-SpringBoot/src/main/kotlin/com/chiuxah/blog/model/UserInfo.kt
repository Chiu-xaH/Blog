package com.chiuxah.blog.model

data class UserInfo(
    val id : Long,
    val username : String,
    var password : String,
    val photo : String,
    val createtime : String,
    val state : Int
)
