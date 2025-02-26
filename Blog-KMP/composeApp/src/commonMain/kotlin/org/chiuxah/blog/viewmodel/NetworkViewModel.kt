package org.chiuxah.blog.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.CommentResponse
import org.chiuxah.blog.logic.network.bean.FollowCountResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.RecommendArticlesResponse
import org.chiuxah.blog.logic.network.bean.RecommendFollowArticlesResponse
import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.network.servicecreator.BlogServiceCreator

class NetworkViewModel : ViewModel() {
    // 网络接口
    private val blogApi = BlogServiceCreator.apiService
    // 通用网络请求处理函数
    private fun <T> launchRequest(
        flow: MutableStateFlow<ApiResult<T>?>? = null,
        sharedFlow: MutableSharedFlow<ApiResult<T>>? = null,
        request: suspend () -> ApiResult<T>
    ) {
        viewModelScope.launch {
            val result = request()
            flow?.value = result               // StateFlow 更新
            sharedFlow?.emit(result)           // SharedFlow 事件发送
        }
    }
    // 检查登录
    private val _checkLoginResponse = MutableStateFlow<ApiResult<UserResponse>?>(null) // 初始值设为 null
    val checkLoginResponse: StateFlow<ApiResult<UserResponse>?> = _checkLoginResponse.asStateFlow()
    fun fetchCheckLogin() = launchRequest(flow = _checkLoginResponse) { blogApi.checkLogin() }

    // 登录
    private val _loginResponse = MutableSharedFlow<ApiResult<LoginResponse>>() // 事件流
    val loginResponse = _loginResponse.asSharedFlow()
    fun fetchLogin(email: String, password: String) = launchRequest(sharedFlow = _loginResponse) { blogApi.login(email, password) }

    // 注册
    private val _regResponse = MutableSharedFlow<ApiResult<MsgResponse>>() // 事件流
    val regResponse = _regResponse.asSharedFlow()
    fun fetchReg(email: String, password: String,code : String,username : String) = launchRequest(sharedFlow = _regResponse) { blogApi.reg(email, password,code,username) }

    // 登出
    fun fetchLogout() = viewModelScope.launch { blogApi.logout() }

    // 缓存列表作者信息
    private val _getAuthorResponse = MutableStateFlow<ApiResult<UserResponse>?>(null) // 初始值设为 null
    val getAuthorResponse: StateFlow<ApiResult<UserResponse>?> = _getAuthorResponse.asStateFlow()
    // 存储博客列表的作者信息
    private val _authorCache = mutableStateMapOf<Int, UserBean>()
    val authorCache: Map<Int, UserBean> get() = _authorCache
    // 批量请求,成功后加入authorCache
    fun fetchGetAuthor(id : Int) {
        if (!_authorCache.containsKey(id)) {
            viewModelScope.launch {
                val response = blogApi.getAuthor(id)
                if (response is ApiResult.Success && response.data.state == StatusCode.OK.code) {
                    _authorCache[id] = response.data.data
                } else {
                    _authorCache[id] = UserBean(0, "未知", "", "")
                }
            }
        }
    }

    // 粉丝与关注
    private val _getFollowCountResponse = MutableStateFlow<ApiResult<FollowCountResponse>?>(null) // 初始值设为 null
    val getFollowCountResponse: StateFlow<ApiResult<FollowCountResponse>?> = _getFollowCountResponse.asStateFlow()
    fun fetchGetFollowCount(uid : Int) = launchRequest(flow = _getFollowCountResponse) { blogApi.getFollowCount(uid) }

    // 发送验证码
    private val _sendCodeResponse = MutableStateFlow<ApiResult<MsgResponse>?>(null) // 初始值设为 null
    val sendCodResponse: StateFlow<ApiResult<MsgResponse>?> = _sendCodeResponse.asStateFlow()
    fun fetchSendCode(email : String) = launchRequest(flow = _sendCodeResponse) { blogApi.sendCode(email) }

    // 热门
    private val _getRecommendHotArticlesResponse = MutableStateFlow<ApiResult<RecommendArticlesResponse>?>(null) // 初始值设为 null
    val getRecommendHotArticlesResponse: StateFlow<ApiResult<RecommendArticlesResponse>?> = _getRecommendHotArticlesResponse.asStateFlow()
    fun fetchRecommendHotArticles(page : Int = 1) = launchRequest(flow = _getRecommendHotArticlesResponse) { blogApi.recommendHotArticles(pageSize = 15, page = page) }

    // 关注的文章推荐
    private val _getRecommendFollowArticlesResponse = MutableStateFlow<ApiResult<RecommendArticlesResponse>?>(null) // 初始值设为 null
    val getRecommendFollowArticlesResponse: StateFlow<ApiResult<RecommendArticlesResponse>?> = _getRecommendFollowArticlesResponse.asStateFlow()
    fun fetchRecommendFollowArticles() = launchRequest(flow = _getRecommendFollowArticlesResponse) { blogApi.recommendFollowArticles() }

    // 评论
    private val _getCommentsResponse = MutableStateFlow<ApiResult<CommentResponse>?>(null) // 初始值设为 null
    val getCommentsResponse: StateFlow<ApiResult<CommentResponse>?> = _getCommentsResponse.asStateFlow()
    fun fetchComments(articleId : Int) = launchRequest(flow = _getCommentsResponse) { blogApi.getComment(articleId) }

    // 评论
    private val _commentResponse = MutableStateFlow<ApiResult<MsgResponse>?>(null) // 初始值设为 null
    val commentResponse: StateFlow<ApiResult<MsgResponse>?> = _commentResponse.asStateFlow()
    fun fetchAddComment(
        articleId: Int?,
        commentId: Int?,
        content: String,
        image: Bitmap?
    ) = launchRequest(flow = _commentResponse) { blogApi.comment(articleId, commentId, content, image) }
}