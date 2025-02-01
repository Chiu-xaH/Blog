package org.chiuxah.blog.logic.network.api

import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.ApiResult

interface ApiService {
    // 登录
    suspend fun login(username : String,password : String) : ApiResult<LoginResponse>
    // 注册
    suspend fun reg(username: String,password: String) : ApiResult<MsgResponse>
    // 检查登录状态
    suspend fun checkLogin() : ApiResult<MsgResponse>
    // 登出
    suspend fun logout() : ApiResult<MsgResponse>
    // 获取所有博文
    suspend fun getAllArticles() : ApiResult<BlogResponse>
    // 根据博文uid得到作者信息
    suspend fun getAuthor(id : Int) : ApiResult<UserResponse>
}