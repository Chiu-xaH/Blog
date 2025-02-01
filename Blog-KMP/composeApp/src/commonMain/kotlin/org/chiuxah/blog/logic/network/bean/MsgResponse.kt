package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class MsgResponse(
    override val state: Int,
    override val msg: String,
    override val data: String? = null
) : BaseResponse()