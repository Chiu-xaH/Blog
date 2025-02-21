package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.model.entity.ArticleEntity
import com.chiuxah.blog.service.VisitService
import com.chiuxah.blog.utils.ControllerUtils.DATABASE_ERROR_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.SUCCESS_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.USER_FORBID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.jsonToMap
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import com.chiuxah.blog.utils.ValidUtils.isValidId
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/history")
class VisitController {
    @Autowired
    lateinit var visitService: VisitService
    @Autowired
    lateinit var articleController: ArticleController
    // 阅读+1
    @PostMapping("/read")
    fun readCount(articleId : Int,request: HttpServletRequest) : Any {
        if(!isValidId(articleId)) return INVALID_RESPONSE
        val uid = myUserInfo(request).id
        // 是否之前阅读过
        // 阅读过 则更新日期
        // 没阅读过 则插入新数据
        val result = visitService.read(uid, articleId)
        return if(result) {
            SUCCESS_RESPONSE
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    // 我阅读了哪些文章
    @GetMapping("/mine")
    fun getMyReadHistories(request: HttpServletRequest) : Any {
        val uid = myUserInfo(request).id
        val result = visitService.getUserReadHistories(uid)
        return ResultEntity.success("查询成功", data = result)
    }
    // 文章的阅读量 公开
    @GetMapping("/article/count")
    fun getArticleReadCount(articleId : Int) : Any {
        if(!isValidId(articleId)) return INVALID_RESPONSE
        val result = visitService.getReadCount(articleId)
        return ResultEntity.success("查询成功", data = result)
    }
    // 文章都被谁阅读了 作者私有
    @GetMapping("/article/all")
    fun getArticleReadList(articleId: Int,request: HttpServletRequest) : Any {
        if(!isValidId(articleId)) return INVALID_RESPONSE
        // 查找作者
        val articleInfoResponse = articleController.getArticleInfo(articleId)
        if(!isSuccessResponse(articleInfoResponse)) {
            return articleInfoResponse
        }
        // 检查作者
        val articleInfo = jsonToMap(articleInfoResponse)["data"] as ArticleEntity
        if(articleInfo.uid != myUserInfo(request).id) {
            return USER_FORBID_RESPONSE
        }
        // 数据库
        val result = visitService.getReadUsers(articleId)
        return ResultEntity.success("查询成功", data = result)
    }
}