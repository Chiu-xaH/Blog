package org.chiuxah.blog.ui.main.home.blog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.chiuxah.blog.logic.bean.HomeRoute
import org.chiuxah.blog.logic.bean.PlatformType
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.RecommendArticlesBean
import org.chiuxah.blog.logic.network.bean.RecommendArticlesResponse
import org.chiuxah.blog.logic.network.bean.RecommendFollowArticlesResponse
import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.uitls.PlatformsManager
import org.chiuxah.blog.ui.uitls.compents.MultiListItem
import org.chiuxah.blog.ui.uitls.compents.UserImage
import org.chiuxah.blog.viewmodel.NetworkViewModel

@Composable
fun BlogsUI(vm : NetworkViewModel) {
    Scaffold (

    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            var loading by remember { mutableStateOf(true) }
            val getListResponse by vm.getRecommendHotArticlesResponse.collectAsState()
            if(loading) {
                vm.fetchRecommendHotArticles()
                LaunchedEffect(getListResponse) {
                    when(getListResponse) {
                        is ApiResult.Success -> {
                            val result = (getListResponse as ApiResult.Success<RecommendArticlesResponse>).data
                            if(result.state == StatusCode.OK.code) {
                                loading = false
                            }
                        }
                        else -> {}
                    }
                }
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ArticleListUI(vm,HomeRoute.RECOMMEND)
            }
        }
    }
}

@Composable
fun ArticleListUI(vm : NetworkViewModel,type : HomeRoute) {
    var articleId by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("博文") }
    var showDialog by remember { mutableStateOf(false) }

    ArticleContentUI(articleId,title,showDialog,vm,{ showDialog = false })

    val dataList = getArticlesList(vm,type)

    LazyColumn {
        items(dataList.size) { index ->
            val item = dataList[index]
            val articleInfo = item.articleInfo
            val countInfo = item.countInfo
            // 每个卡片获取作者信息 左侧显示头像
            val author = vm.authorCache[articleInfo.uid] ?: UserBean(0, "未获取", "", "")
            // 获取用户信息
            LaunchedEffect(articleInfo.uid) {
                vm.fetchGetAuthor(articleInfo.uid)
            }
            val itemTitle = articleInfo.title

            MultiListItem(
                headlineContent = { Text(itemTitle) },
                overlineContent = { Text("作者 ${author.username}" + " 更新 " + articleInfo.update_time) },
                leadingContent = {
                    UserImage(author.photo)
                },
                supportingContent = {
                    Text("点赞 ${countInfo.likeCount} 评论 ${countInfo.commentCount} 收藏 ${countInfo.collectCount}")
                },
                modifier = Modifier.clickable {
                    title = itemTitle
                    articleId = articleInfo.id
                    showDialog = true
                }
            )
            if(PlatformsManager.platformType == PlatformType.DESKTOP && index-1 != dataList.size) {
                Divider()
            }
        }
    }
}


@Composable
fun FollowArticlesUI(vm: NetworkViewModel) {
    Scaffold (

    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            var loading by remember { mutableStateOf(true) }
            val getListResponse by vm.getRecommendFollowArticlesResponse.collectAsState()
            if(loading) {
                vm.fetchRecommendFollowArticles()
                LaunchedEffect(getListResponse) {
                    when(getListResponse) {
                        is ApiResult.Success -> {
                            val result = (getListResponse as ApiResult.Success<RecommendArticlesResponse>).data
                            if(result.state == StatusCode.OK.code) {
                                loading = false
                            }
                        }
                        else -> {}
                    }
                }
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ArticleListUI(vm,HomeRoute.FOLLOW)
            }
        }
    }
}
