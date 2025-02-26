package org.chiuxah.blog.ui.main.home.blog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.m3.Markdown
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.CommentBean
import org.chiuxah.blog.logic.network.bean.CommentResponse
import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.ui.uitls.compents.CustomMarkdownTypography
import org.chiuxah.blog.ui.uitls.compents.MultiListItem
import org.chiuxah.blog.ui.uitls.compents.UserImage
import org.chiuxah.blog.viewmodel.NetworkViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleContentUI(articleId : Int,title : String, showDialog : Boolean, vm: NetworkViewModel,showChanged : () -> Unit) {
    if(showDialog) {
        Dialog(
            onDismissRequest = showChanged,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.mediumTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        actions = {
                            IconButton(onClick = showChanged) { Icon(Icons.Default.Close, contentDescription = null) }
                        },
                        title = { Text(title) }
                    )
                }
            ) { innerPadding ->
//                val state = rememberRichTextState().setMarkdown(articleInfo.content)
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
//                    RichText(
//                        state = state,
//                        modifier = Modifier
//                            .padding(horizontal = 15.dp)
//                            .verticalScroll(rememberScrollState())
//                    )
                    Column {
                        Markdown(
                            content = "正在开发",
                            modifier = Modifier.padding(horizontal = 15.dp).verticalScroll(
                                rememberScrollState()
                            ),
                            imageTransformer = Coil3ImageTransformerImpl,
                            typography = CustomMarkdownTypography()
                        )
                        Spacer(Modifier.height(10.dp))
                        CommentUI(vm,articleId)
                    }
                }
            }
        }
    }
}

@Composable
fun CommentUI(vm : NetworkViewModel,articleId: Int) {
    Scaffold (

    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            var loading by remember { mutableStateOf(true) }
            val getListResponse by vm.getCommentsResponse.collectAsState()
            if(loading) {
                vm.fetchComments(articleId)
                LaunchedEffect(getListResponse) {
                    when(getListResponse) {
                        is ApiResult.Success -> {
                            val result = (getListResponse as ApiResult.Success<CommentResponse>).data
                            if(result.state == StatusCode.OK.code) {
                                loading = false
                            }
                        }
                        else -> {}
                    }
                }
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                CommentListUI(vm)
            }
        }
    }
}

@Composable
fun CommentItem(item : CommentBean, vm: NetworkViewModel) {
    var isExpand by remember { mutableStateOf(false) }
    val commentInfo = item.commentInfo
    val uid = commentInfo.uid
    val children = item.children
    // 每个卡片获取作者信息 左侧显示头像
    val author = vm.authorCache[uid] ?: UserBean(0, "未获取", "", "")
    // 获取用户信息
    LaunchedEffect(uid) {
        vm.fetchGetAuthor(uid)
    }
    Divider()
    MultiListItem(
        headlineContent = { Text(commentInfo.content) },
        overlineContent = { Text(author.username + " 发布 " + commentInfo.create_time) },
        leadingContent = {
            UserImage(author.photo)
        },
        modifier = Modifier.clickable {
            // 长按复制评论内容
            // 点击回复评论 打开输入框
        },
        trailingContent = if(children.isNotEmpty()){
            {
                TextButton(
                    onClick = {
                        isExpand = !isExpand
                    }
                ) {
                    Text("展开${children.size}条")
                }
            }
        } else {
            null
        },
        supportingContent = {
            if(isExpand) {
                // 子列表
                Column {
                    for(child in children) {
                        CommentItem(child,vm)
                    }
                }
            }
        }
    )
}

@Composable
fun CommentListUI(vm : NetworkViewModel) {

    val dataList = getComments(vm)
    LazyColumn {
        items(dataList.size) { index ->
            CommentItem(dataList[index],vm)
        }
    }
}

fun getComments(vm : NetworkViewModel) : List<CommentBean>{
    val json = vm.getCommentsResponse.value
    return try {
        val data = (json as ApiResult.Success<CommentResponse>).data.data
        data
    } catch (e : Exception) {
        emptyList()
    }
}