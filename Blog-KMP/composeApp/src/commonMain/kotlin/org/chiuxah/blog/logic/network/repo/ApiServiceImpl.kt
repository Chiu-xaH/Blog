package org.chiuxah.blog.logic.network.repo

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.network.api.ApiService
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.NetworkConstants
import org.chiuxah.blog.logic.network.config.NetworkConstants.parseJson

//接口实现
class ApiServiceImpl(private val client : HttpClient) : ApiService {
    override suspend fun login(username : String,password : String): ApiResult<LoginResponse> = runBlocking {
        try {
            val url = NetworkConstants.APIs.LOGIN
            val response  = client.post(url) {
                parameter("username", username)
                parameter("password", password)
            }.bodyAsText()
            val json = parseJson<LoginResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun reg(username: String, password: String): ApiResult<MsgResponse> = runBlocking  {
        try {
            val url = NetworkConstants.APIs.REG
            val response  = client.post(url) {
                parameter("username", username)
                parameter("password", password)
            }.bodyAsText()
            val json = parseJson<MsgResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun checkLogin(): ApiResult<MsgResponse> = runBlocking  {
        try {
            val url = NetworkConstants.APIs.CHECK_LOGIN
            val response  = client.get(url).bodyAsText()
            val json = parseJson<MsgResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun logout(): ApiResult<MsgResponse> = runBlocking  {
        try {
            val url = NetworkConstants.APIs.LOGOUT
            val response  = client.post(url).bodyAsText()
            val json = parseJson<MsgResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }

    override suspend fun getAllArticles(): ApiResult<BlogResponse> = runBlocking {
        try {
            val url = NetworkConstants.APIs.GET_ALL_ARTICLES
            val response  = client.get(url).bodyAsText()
            val json = parseJson<BlogResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }


    override suspend fun getAuthor(id : Int): ApiResult<UserResponse> = runBlocking {
        try {
            val url = NetworkConstants.APIs.GET_USER_BY_ID
            val response  = client.get(url) {
                parameter("id",id)
            }.bodyAsText()
            val json = parseJson<UserResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }
}
