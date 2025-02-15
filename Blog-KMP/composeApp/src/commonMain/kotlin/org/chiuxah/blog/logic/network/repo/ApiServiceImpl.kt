package org.chiuxah.blog.logic.network.repo

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import org.chiuxah.blog.logic.network.api.ApiService
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.FollowCountResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.network.config.NetworkConstants
import org.chiuxah.blog.logic.network.config.NetworkConstants.parseJson

//接口实现
class ApiServiceImpl(private val client : HttpClient) : ApiService {
    // 通用基础函数
    private suspend inline fun <reified T> request(
        method: HttpMethod,
        url: String,
        params: Map<String, Any?> = emptyMap()
    ): ApiResult<T> {
        return try {
            val response = client.request(url) {
                this.method = method
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.bodyAsText()

            val parsedResponse = parseJson<T>(response)
            ApiResult.Success(parsedResponse)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }

    // 登录
    override suspend fun login(username : String,password : String): ApiResult<LoginResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.LOGIN,
            params = mapOf(
                "username" to username,
                "password" to password
            )
        )
    }
    // 注册
    override suspend fun reg(username: String, password: String): ApiResult<MsgResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.REG,
            params = mapOf(
                "username" to username,
                "password" to password
            )
        )
    }
    // 检查登录
    override suspend fun checkLogin(): ApiResult<UserResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.CHECK_LOGIN
        )
    }
    // 登出
    override suspend fun logout(): ApiResult<MsgResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.LOGOUT
        )
    }
    // 获取所有文章
    override suspend fun getAllArticles(): ApiResult<BlogResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_ALL_ARTICLES
        )
    }
    // 获取作者信息
    override suspend fun getAuthor(id : Int): ApiResult<UserResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_USER_BY_ID,
            params = mapOf("id" to id)
        )
    }
    // 获取登录信息
    override suspend fun getMy(): ApiResult<UserResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_MY
        )
    }
    // 删除博文
    override suspend fun delArticle(id: Int): ApiResult<MsgResponse> {
        return request(
            method = HttpMethod.Delete,
            url = NetworkConstants.APIs.DEL,
            params = mapOf("id" to id)
        )
    }
    // 查看关注及其粉丝数目
    override suspend fun getFollowCount(id: Int): ApiResult<FollowCountResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_FOLLOWERS_COUNT,
            params = mapOf("uid" to id)
        )
    }
}
