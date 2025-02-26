package org.chiuxah.blog.logic.network.api

import coil3.Bitmap
import org.chiuxah.blog.logic.network.bean.BlogResponse
import org.chiuxah.blog.logic.network.bean.CommentResponse
import org.chiuxah.blog.logic.network.bean.FollowCountResponse
import org.chiuxah.blog.logic.network.bean.LoginResponse
import org.chiuxah.blog.logic.network.bean.MsgResponse
import org.chiuxah.blog.logic.network.bean.RecommendArticlesResponse
import org.chiuxah.blog.logic.network.bean.RecommendFollowArticlesResponse
import org.chiuxah.blog.logic.network.bean.UserResponse
import org.chiuxah.blog.logic.network.config.ApiResult

interface ApiService {
    // 登录
    suspend fun login(email : String,password : String) : ApiResult<LoginResponse>
    // 注册
    suspend fun reg(email: String,password: String,code : String,username : String) : ApiResult<MsgResponse>
    // 检查登录状态
    suspend fun checkLogin() : ApiResult<UserResponse>
    // 登出
    suspend fun logout() : ApiResult<MsgResponse>
    // 获取所有博文
    suspend fun getAllArticles() : ApiResult<BlogResponse>
    // 根据博文uid得到作者信息
    suspend fun getAuthor(id : Int) : ApiResult<UserResponse>
    // 根据Cookie获取登录者信息
    suspend fun getMy() : ApiResult<UserResponse>
    // 删除博文
    suspend fun delArticle(id : Int) : ApiResult<MsgResponse>
    // 查看粉丝及其关注数目
    suspend fun getFollowCount(id : Int) : ApiResult<FollowCountResponse>
    // 发送验证码
    suspend fun sendCode(email : String) : ApiResult<MsgResponse>
    /************************************************************/
    // 首页展示 推荐热门博文
    suspend fun recommendHotArticles(pageSize : Int = 15,page : Int = 1) : ApiResult<RecommendArticlesResponse>
    // 首页展示 推荐关注
    suspend fun recommendFollowArticles() : ApiResult<RecommendArticlesResponse>
    // 所有评论
    suspend fun getComment(articleId : Int) : ApiResult<CommentResponse>
    // 评论
    suspend fun comment(articleId : Int?, commentId: Int?, content : String, image : Bitmap?) : ApiResult<MsgResponse>
}