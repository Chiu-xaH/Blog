package org.chiuxah.blog.ui.main.home.blog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.graphics.Color
import org.chiuxah.blog.logic.bean.PlatformType
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.uitls.PlatformsManager
import org.chiuxah.blog.ui.uitls.compents.MultiListItem
import org.chiuxah.blog.ui.uitls.compents.MyCustomCard
import org.chiuxah.blog.ui.uitls.compents.UserImage
import org.chiuxah.blog.viewmodel.NetworkViewModel

@Composable
fun BlogsUI(vm : NetworkViewModel) {
    Scaffold (

    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            var loading by remember { mutableStateOf(true) }
            val getListResponse by vm.getAllArticlesResponse.collectAsState()
            if(loading) {
                vm.fetchGetAllArticles()
                LaunchedEffect(getListResponse) {
                    when(getListResponse) {
                        is ApiResult.Success -> {
                            val result = (getListResponse as ApiResult.Success<BlogResponse>).data
                            if(result.state == StatusCode.OK.code) {
                                loading = false
                            }
                        }
                        else -> {}
                    }
                }
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ArticleListUI(vm)
            }
        }
    }
}

@Composable
fun ArticleListUI(vm : NetworkViewModel) {
    var articleId by remember { mutableStateOf(0) }
    var title by remember { mutableStateOf("博文") }
    var showDialog by remember { mutableStateOf(false) }

    ArticleContentUI(articleId,title,showDialog,{ showDialog = false })

    val dataList = getArticlesList(vm)



    LazyColumn {
        items(dataList.size) { index ->
            val item = dataList[index]
            // 每个卡片获取作者信息 左侧显示头像
            val author = vm.authorCache[item.uid] ?: UserBean(0, "未获取", "", "")
            // 获取用户信息
            LaunchedEffect(item.uid) {
                vm.fetchGetAuthor(item.uid)
            }
            val itemTitle = item.title

            MultiListItem(
                headlineContent = { Text(itemTitle) },
                overlineContent = { Text("作者 ${author.username}" + " 更新 " + item.update_time) },
                leadingContent = {
                    UserImage(author.photo)
                },
                modifier = Modifier.clickable {
                    title = itemTitle
                    articleId = item.id
                    showDialog = true
                }
            )
            if(PlatformsManager.platformType == PlatformType.DESKTOP && index-1 != dataList.size) {
                Divider()
            }
        }
    }
}
