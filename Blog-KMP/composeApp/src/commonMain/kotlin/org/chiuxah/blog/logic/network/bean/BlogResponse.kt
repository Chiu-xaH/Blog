package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class BlogResponse(
    override val state: Int,
    override val msg: String,
    override val data: List<ArticleBean>
) : BaseResponse()

@Serializable
data class ArticleBean(
    val id : Int,
    val title : String,
    val content : String,
    val createtime : String,
    val updatetime : String,
    val uid : Int,
    val rcount : Int
)
