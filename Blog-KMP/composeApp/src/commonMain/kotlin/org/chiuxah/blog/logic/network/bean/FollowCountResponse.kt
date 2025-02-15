package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class FollowCountResponse(
    override val msg: String,
    override val state: Int,
    override val data: FollowCountBean
) : BaseResponse()

@Serializable
data class FollowCountBean(
    val followersCount : Int,
    val followeeCount : Int
)
