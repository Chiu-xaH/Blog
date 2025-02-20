package com.chiuxah.blog.model.entity

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Date
import java.sql.Timestamp

// TABLE user_info
data class UserEntity(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val username : String,
    val password : String,
    val email : String,
    val phone_number : String?,
    val description : String?,
    val sex : Int,
    val born_date : Date?,
    val region : String?,
    val website : String?,
    val photo : String,
    val state : Int
) : BaseSqlEntity() {
    constructor() : this(
        id = 0,
        create_time = null,
        username = "游客",
        password = "",
        email = "",
        phone_number = null,
        description = null,
        sex = 0,
        born_date = null,
        region = null,
        website = null,
        photo = "/guest.png",
        state = 1
    ) // 添加无参构造函数
}