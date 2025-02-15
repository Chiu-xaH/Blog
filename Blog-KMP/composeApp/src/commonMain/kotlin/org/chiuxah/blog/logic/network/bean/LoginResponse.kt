package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class LoginResponse(
    override val state : Int,
    override val msg : String,
    override val data : LoginBean?
) : BaseResponse()

@Serializable
data class LoginBean(
    val JSESSIONID : String,
    val userinfo : UserBean
)