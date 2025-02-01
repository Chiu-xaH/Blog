package org.chiuxah.blog.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chiuxah.blog.logic.bean.StatusCode
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.network.servicecreator.BlogServiceCreator
import org.chiuxah.blog.logic.uitls.PreferencesManager
import org.chiuxah.blog.logic.uitls.PreferencesManager.KEY_COOKIE
import org.chiuxah.blog.logic.uitls.reset

class MainViewModel : ViewModel() {
    private val blogApi = BlogServiceCreator.apiService

    private val _checkLoginResponse = MutableStateFlow<ApiResult<MsgResponse>?>(null) // 初始值设为 null
    val checkLoginResponse: StateFlow<ApiResult<MsgResponse>?> = _checkLoginResponse.asStateFlow()

    fun fetchCheckLogin() {
        viewModelScope.launch {
            _checkLoginResponse.update { blogApi.checkLogin() }
        }
    }

    private val _loginResponse = MutableSharedFlow<ApiResult<LoginResponse>>() // 事件流
    val loginResponse = _loginResponse.asSharedFlow()

    fun fetchLogin(username: String, password: String) {
        viewModelScope.launch {
            _loginResponse.emit(blogApi.login(username, password)) // 每次触发都会发射事件
        }
    }


    private val _regResponse = MutableSharedFlow<ApiResult<MsgResponse>>() // 事件流
    val regResponse = _regResponse.asSharedFlow()

    fun fetchReg(username: String, password: String) {
        viewModelScope.launch {
            _regResponse.emit(blogApi.reg(username, password)) // 每次触发都会发射事件
        }
    }

    fun fetchLogout() {
        viewModelScope.launch {
            blogApi.logout()
        }
    }

    private val _getAllArticlesResponse = MutableStateFlow<ApiResult<BlogResponse>?>(null) // 初始值设为 null
    val getAllArticlesResponse: StateFlow<ApiResult<BlogResponse>?> = _getAllArticlesResponse.asStateFlow()

    fun fetchGetAllArticles() {
        viewModelScope.launch {
            _getAllArticlesResponse.update { blogApi.getAllArticles() }
        }
    }

    private val _getAuthorResponse = MutableStateFlow<ApiResult<UserResponse>?>(null) // 初始值设为 null
    val getAuthorResponse: StateFlow<ApiResult<UserResponse>?> = _getAuthorResponse.asStateFlow()
    private val _authorCache = mutableStateMapOf<Int, UserBean>()
    val authorCache: Map<Int, UserBean> get() = _authorCache

    fun fetchGetAuthor(id : Int) {
        if (!_authorCache.containsKey(id)) {
            viewModelScope.launch {
                val response = blogApi.getAuthor(id)
                if (response is ApiResult.Success && response.data.state == StatusCode.SUCCESS.code) {
                    _authorCache[id] = response.data.data
                } else {
                    _authorCache[id] = UserBean(0, "未知", "", "")
                }
            }
        }
    }
}