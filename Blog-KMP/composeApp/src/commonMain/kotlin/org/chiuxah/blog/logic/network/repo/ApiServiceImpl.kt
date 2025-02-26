package org.chiuxah.blog.logic.network.repo

import coil3.Bitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import org.chiuxah.blog.logic.network.api.ApiService
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.CommentResponse
import org.chiuxah.blog.logic.network.bean.FollowCountResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.RecommendArticlesResponse
import org.chiuxah.blog.logic.network.bean.RecommendFollowArticlesResponse
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
    override suspend fun login(email : String,password : String): ApiResult<LoginResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.LOGIN,
            params = mapOf(
                "email" to email,
                "password" to password
            )
        )
    }
    // 注册
    override suspend fun reg(email: String, password: String,code : String,username : String): ApiResult<MsgResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.REG,
            params = mapOf(
                "username" to username,
                "password" to password,
                "code" to code,
                "email" to email
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
    // 发送验证码
    override suspend fun sendCode(email: String): ApiResult<MsgResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.SEND_CODE,
            params = mapOf("email" to email)
        )
    }
    // 推荐热门文章
    override suspend fun recommendHotArticles(
        pageSize: Int,
        page: Int
    ): ApiResult<RecommendArticlesResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_HOT_ARTICLES,
            params = mapOf(
                "pageSize" to pageSize,
                "page" to page
            )
        )
    }
    // 推荐关注的文章
    override suspend fun recommendFollowArticles(): ApiResult<RecommendArticlesResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_FOLLOW_ARTICLES
        )
    }
    // 评论 通过递归构建层序关系
    override suspend fun getComment(articleId: Int): ApiResult<CommentResponse> {
        return request(
            method = HttpMethod.Get,
            url = NetworkConstants.APIs.GET_ALL_COMMENTS,
            params = mapOf("articleId" to articleId)
        )
    }
    // 评论
    override suspend fun comment(
        articleId: Int?,
        commentId: Int?,
        content: String,
        image: Bitmap?
    ): ApiResult<MsgResponse> {
        return request(
            method = HttpMethod.Post,
            url = NetworkConstants.APIs.ADD_COMMENT,
            params = mapOf(
                "articleId" to articleId,
                "commentId" to commentId,
                "articleId" to articleId,
                "content" to content,
                "image" to image
            )
        )
    }
}
