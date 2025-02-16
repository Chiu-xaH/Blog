package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.BaseSqlBean
import java.sql.Timestamp

// TABLE user_info
data class UserBean(
    override val id : Int,
    val username : String,
    val password : String,
    val photo : String,
    override val create_time : Timestamp? = null,
    val state : Int
) : BaseSqlBean() {
    constructor() : this(0, "游客", "", "", null, 0) // 添加无参构造函数
}

// 前端返回的对象，去掉了密码字段
data class UserInfoDTO(
    override val id : Int,
    val username : String,
    val photo : String,
    override val create_time : Timestamp? = null,
    val state : Int
) : BaseSqlBean() {
    constructor() : this(0, "游客", "", null, 0) // 添加无参构造函数
}


// 基础用户信息 用于列表展示时
data class UserInfoSummary(
    val id : Int,
    val username : String,
    val photo : String
)