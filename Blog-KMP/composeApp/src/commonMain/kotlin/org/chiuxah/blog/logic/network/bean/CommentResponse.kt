package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class CommentResponse(
    override val msg: String,
    override val state: Int,
    override val data: List<CommentBean>
) : BaseResponse()

@Serializable
data class CommentBean(
    val commentInfo : CommentInfo,
    val children : List<CommentBean>
)
@Serializable
data class CommentInfo(
    val id : Int,
    val create_time : String,
    val uid : Int,
    val content : String,
    val image_url : String?
)
