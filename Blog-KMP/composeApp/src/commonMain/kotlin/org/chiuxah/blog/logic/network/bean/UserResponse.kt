package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class UserResponse(
    override val state: Int,
    override val msg: String,
    override val data: UserBean
) : BaseResponse()

@Serializable
data class UserBean(
    val id : Int,
    val username : String,
    val photo : String,
    val email : String
)
