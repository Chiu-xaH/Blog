package org.chiuxah.blog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.SuccessResponse
import org.chiuxah.blog.logic.network.servicecreator.BlogServiceCreator
import org.chiuxah.blog.logic.uitls.reset

class MainViewModel : ViewModel() {
    private val blogApi = BlogServiceCreator.apiService

//    private val _loginResponse = MutableStateFlow<ApiResult<LoginResponse>?>(null) // 初始值设为 null
//    val loginResponse: StateFlow<ApiResult<LoginResponse>?> = _loginResponse.asStateFlow()
//
//    fun fetchLogin(username: String, password: String) {
//        viewModelScope.launch {
//            _loginResponse.value = null
//            _loginResponse.update { blogApi.login(username, password) }
//        }
//    }
    private val _loginResponse = MutableSharedFlow<ApiResult<LoginResponse>>() // 事件流
    val loginResponse = _loginResponse.asSharedFlow()

    fun fetchLogin(username: String, password: String) {
        viewModelScope.launch {
            _loginResponse.emit(blogApi.login(username, password)) // 每次触发都会发射事件
        }
    }


    private val _regResponse = MutableSharedFlow<ApiResult<SuccessResponse>>() // 事件流
    val regResponse = _regResponse.asSharedFlow()

    fun fetchReg(username: String, password: String) {
        viewModelScope.launch {
            _regResponse.emit(blogApi.reg(username, password)) // 每次触发都会发射事件
        }
    }
}