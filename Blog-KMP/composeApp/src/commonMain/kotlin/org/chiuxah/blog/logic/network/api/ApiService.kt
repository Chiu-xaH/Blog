package org.chiuxah.blog.logic.network.api

import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.SuccessResponse
import org.chiuxah.blog.logic.network.config.ApiResult

interface ApiService {
    // 登录
    suspend fun login(username : String,password : String) : ApiResult<LoginResponse>
    // 注册
    suspend fun reg(username: String,password: String) : ApiResult<SuccessResponse>
}