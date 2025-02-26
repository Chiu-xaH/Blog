package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class RecommendArticlesResponse(
    override val msg: String,
    override val state: Int,
    override val data: List<RecommendArticlesBean>?
) : BaseResponse()

@Serializable
data class RecommendArticlesBean(
    val articleInfo : ArticleInfoSummaryBean,
    val countInfo : ArticleCountBean
)

@Serializable
data class ArticleInfoSummaryBean(
    val id: Int,
    val title: String,
    val update_time: String,
    val uid: Int
)

@Serializable
data class ArticleCountBean(
    val likeCount: Int,
    val collectCount: Int,
    val visitCount: Int,
    val commentCount: Int
)