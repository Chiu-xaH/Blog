package org.chiuxah.blog.ui.main.home.blog

import org.chiuxah.blog.logic.bean.HomeRoute
import org.chiuxah.blog.logic.network.bean.ArticleBean
import org.chiuxah.blog.logic.network.bean.ArticleBeanSummary
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.RecommendArticlesBean
import org.chiuxah.blog.logic.network.bean.RecommendArticlesResponse
import org.chiuxah.blog.logic.network.bean.RecommendFollowArticlesResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.viewmodel.NetworkViewModel


fun getArticlesList(vm : NetworkViewModel,type: HomeRoute) : List<RecommendArticlesBean> {
    return try {
        val data = when(type) {
            HomeRoute.FOLLOW -> (vm.getRecommendFollowArticlesResponse.value as ApiResult.Success<RecommendArticlesResponse>).data.data!!
            HomeRoute.RECOMMEND -> (vm.getRecommendHotArticlesResponse.value as ApiResult.Success<RecommendArticlesResponse>).data.data!!
            else -> return emptyList()
        }
        data
    } catch (e : Exception) {
        emptyList()
    }
}

