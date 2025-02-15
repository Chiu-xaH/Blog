package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class BlogResponse(
    override val state: Int,
    override val msg: String,
    override val data: List<ArticleBeanSummary>
) : BaseResponse()

@Serializable
data class ArticleBean(
    val id : Int,
    val title : String,
    val content : String,
    val create_time : String,
    val update_time : String,
    val uid : Int,
    val rcount : Int
)

@Serializable
data class ArticleBeanSummary(
    val id : Int,
    val title : String,
    val update_time : String,
    val uid : Int,
    val rcount : Int
)

