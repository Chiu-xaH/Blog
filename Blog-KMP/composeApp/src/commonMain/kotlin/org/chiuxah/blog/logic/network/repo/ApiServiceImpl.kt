package org.chiuxah.blog.logic.network.repo

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.chiuxah.blog.logic.network.config.ApiResult
import org.chiuxah.blog.logic.network.api.ApiService
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.SuccessResponse
import org.chiuxah.blog.logic.network.config.NetworkConstants
import org.chiuxah.blog.logic.network.config.NetworkConstants.parseJson

//接口实现
class ApiServiceImpl(private val client : HttpClient) : ApiService {
    override suspend fun login(username : String,password : String): ApiResult<LoginResponse> = runBlocking {
        try {
            val url = NetworkConstants.API_LOGIN
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

    override suspend fun reg(username: String, password: String): ApiResult<SuccessResponse> = runBlocking  {
        try {
            val url = NetworkConstants.API_REG
            val response  = client.post(url) {
                parameter("username", username)
                parameter("password", password)
            }.bodyAsText()
            val json = parseJson<SuccessResponse>(response)
            ApiResult.Success(json)
        } catch (e:Exception) {
            ApiResult.Error(e)
        }
    }
}
