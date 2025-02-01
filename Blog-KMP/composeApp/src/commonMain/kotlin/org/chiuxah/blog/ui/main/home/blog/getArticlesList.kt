package org.chiuxah.blog.ui.main.home.blog

import org.chiuxah.blog.logic.network.bean.ArticleBean
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.viewmodel.MainViewModel


fun getArticlesList(vm : MainViewModel) : List<ArticleBean> {
    return try {
        val data = (vm.getAllArticlesResponse.value as ApiResult.Success<BlogResponse>).data
        data.data
    } catch (e : Exception) {
        emptyList()
    }
}
