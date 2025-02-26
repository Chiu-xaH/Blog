package org.chiuxah.blog.logic.network.bean

import kotlinx.serialization.Serializable
import org.chiuxah.blog.logic.network.bean.main.BaseResponse

@Serializable
data class RecommendFollowArticlesResponse(
    override val msg: String,
    override val state: Int,
    override val data: List<ArticleInfoSummaryBean>?
) : BaseResponse()
