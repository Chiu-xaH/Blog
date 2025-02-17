package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Date
import java.sql.Timestamp

// TABLE user_info
data class UserBean(
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
) : BaseSqlBean() {
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

// 前端返回的对象，去掉了密码字段
data class UserInfoDTO(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val username : String,
    val email : String,
    val phone_number : String?,
    val description : String?,
    val sex : Int,
    val born_date : Date?,
    val region : String?,
    val website : String?,
    val photo : String,
    val state : Int
) : BaseSqlBean() {
    constructor() : this(
        id = 0,
        create_time = null,
        username = "游客",
        email = "",
        phone_number = null,
        description = null,
        sex = 0,
        born_date = null,
        region = null,
        website = null,
        photo = "/guest.png",
        state = 1
    )
}


// 基础用户信息 用于列表展示时
data class UserInfoSummary(
    val id : Int,
    val username : String,
    val photo : String
)

data class UserCheckSummary(
    val id : Int,
    val username : String,
    val password : String,
    val email : String,
)