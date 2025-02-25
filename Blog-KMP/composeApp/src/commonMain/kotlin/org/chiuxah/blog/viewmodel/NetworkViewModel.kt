package org.chiuxah.blog.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.FollowCountResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
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

    private val _checkLoginResponse = MutableStateFlow<ApiResult<UserResponse>?>(null) // 初始值设为 null
    val checkLoginResponse: StateFlow<ApiResult<UserResponse>?> = _checkLoginResponse.asStateFlow()

    fun fetchCheckLogin() = launchRequest(flow = _checkLoginResponse) { blogApi.checkLogin() }


    private val _loginResponse = MutableSharedFlow<ApiResult<LoginResponse>>() // 事件流
    val loginResponse = _loginResponse.asSharedFlow()

    fun fetchLogin(username: String, password: String) = launchRequest(sharedFlow = _loginResponse) { blogApi.login(username, password) }


    private val _regResponse = MutableSharedFlow<ApiResult<MsgResponse>>() // 事件流
    val regResponse = _regResponse.asSharedFlow()

    fun fetchReg(email: String, password: String,code : String,username : String) = launchRequest(sharedFlow = _regResponse) { blogApi.reg(email, password,code,username) }

    fun fetchLogout() = viewModelScope.launch { blogApi.logout() }

    private val _getAllArticlesResponse = MutableStateFlow<ApiResult<BlogResponse>?>(null) // 初始值设为 null
    val getAllArticlesResponse: StateFlow<ApiResult<BlogResponse>?> = _getAllArticlesResponse.asStateFlow()

    fun fetchGetAllArticles() = launchRequest(flow = _getAllArticlesResponse) { blogApi.getAllArticles() }

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

    private val _getFollowCountResponse = MutableStateFlow<ApiResult<FollowCountResponse>?>(null) // 初始值设为 null
    val getFollowCountResponse: StateFlow<ApiResult<FollowCountResponse>?> = _getFollowCountResponse.asStateFlow()

    fun fetchGetFollowCount(uid : Int) = launchRequest(flow = _getFollowCountResponse) { blogApi.getFollowCount(uid) }

    private val _sendCodeResponse = MutableStateFlow<ApiResult<MsgResponse>?>(null) // 初始值设为 null
    val sendCodResponse: StateFlow<ApiResult<MsgResponse>?> = _sendCodeResponse.asStateFlow()

    fun fetchSendCode(email : String) = launchRequest(flow = _sendCodeResponse) { blogApi.sendCode(email) }

}