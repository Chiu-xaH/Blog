package org.chiuxah.blog.ui.main.home.blog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
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
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.ui.uitls.compents.MyCard
import org.chiuxah.blog.ui.uitls.compents.UserImage
import org.chiuxah.blog.viewmodel.MainViewModel

@Composable
fun BlogsUI(vm : MainViewModel) {
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
                            if(result.state == StatusCode.SUCCESS.code) {
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
fun ArticleListUI(vm : MainViewModel) {
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

            MyCard {
                ListItem(
                    headlineContent = { Text(item.title) },
                    supportingContent = { Text(item.content.replace("\n","").replace("#",""), maxLines = 1) },
                    overlineContent = { Text("作者 ${author.username}" + " 更新 " + item.updatetime) },
                    leadingContent = {
                        UserImage(author.photo)
                    },
                    modifier = Modifier.clickable {

                    }
                )
            }
        }
    }
}
