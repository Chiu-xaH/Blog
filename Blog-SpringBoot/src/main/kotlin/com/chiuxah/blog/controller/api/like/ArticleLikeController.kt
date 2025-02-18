package com.chiuxah.blog.controller.api.like

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.controller.api.ArticleController
import com.chiuxah.blog.model.bean.ArticleBean
import com.chiuxah.blog.service.like.ArticleLikeService
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.jsonToMap
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import com.chiuxah.blog.utils.ValidUtils.isValidId
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// 点赞模块：文章点赞
@RestController
@RequestMapping("/api/v1/like/article")
class ArticleLikeController {
    @Autowired
    lateinit var articleLikeService: ArticleLikeService
    @Autowired
    lateinit var articleController: ArticleController
    // 点赞数量 公开
    @GetMapping("/count")
    fun getArticleLikeCount(articleId : Int) : Any {
        if(!isValidId(articleId)) {
            return INVALID_RESPONSE
        }
        val result = articleLikeService.getLikeCount(articleId)
        return ResultEntity.success("查询成功", data = result)
    }
    // 谁点赞了文章 只有作者可见
    @GetMapping("/list")
    fun getArticleLikeList(articleId: Int,request : HttpServletRequest) : Any {
        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        val articleResponse = articleController.getByBlogId(articleId)
        if(!isSuccessResponse(articleResponse)) {
            return articleResponse
        }
        // 身份核验 是否为作者
        val articleInfo = jsonToMap(articleResponse)["data"] as ArticleBean
        if(articleInfo.uid != uid) {
            return ResultEntity.fail(StatusCode.FORBIDDEN,"无权限")
        }

        val result = articleLikeService.getLikeList(articleId)
        return ResultEntity.success("查询成功", mapOf(
            "article_info" to articleInfo,
            "like_uid" to result
        ))
    }
    // 点赞
    @PostMapping("/like")
    fun likeArticle(articleId: Int,request: HttpServletRequest) : Any {
        if(!isValidId(articleId)) {
            return INVALID_RESPONSE
        }
        val uid = myUserInfo(request).id
        val result = articleLikeService.like(uid, articleId)
        return if(result) {
            ResultEntity.success("点赞成功")
        } else {
            ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"点赞失败")
        }
    }
    // 取消点赞
    @DeleteMapping("/unlike")
    fun unlikeArticle(articleId: Int,request: HttpServletRequest) : Any {
        if(!isValidId(articleId)) {
            return INVALID_RESPONSE
        }
        val uid = myUserInfo(request).id
        val result = articleLikeService.unlike(uid, articleId)
        return if(result) {
            ResultEntity.success("取消点赞成功")
        } else {
            ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"取消点赞失败")
        }
    }
}