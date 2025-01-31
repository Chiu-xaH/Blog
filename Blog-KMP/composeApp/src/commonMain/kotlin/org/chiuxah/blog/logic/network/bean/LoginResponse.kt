package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.ApiBaseResponse

@Serializable
data class LoginResponse(
    override val state : Int,
    override val msg : String,
    override val data : LoginBean?
) : ApiBaseResponse()

@Serializable
data class LoginBean(
    val JSESSIONID : String
)