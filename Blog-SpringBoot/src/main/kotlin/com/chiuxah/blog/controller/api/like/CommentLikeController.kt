package com.chiuxah.blog.controller.api.like

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.service.like.CommentLikeService
import com.chiuxah.blog.utils.ControllerUtils
import com.chiuxah.blog.utils.ControllerUtils.DATABASE_ERROR_RESPONSE
import com.chiuxah.blog.utils.ValidUtils
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// 点赞模块：评论点赞
@RestController
@RequestMapping("/api/v1/like/comment")
class CommentLikeController {
    @Autowired
    lateinit var commentLikeService: CommentLikeService
    // 点赞数量 公开
    @GetMapping("/count")
    fun getCommentLikeCount(commentId : Int) : Any {
        if(!ValidUtils.isValidId(commentId)) {
            return ControllerUtils.INVALID_RESPONSE
        }
        val result = commentLikeService.getLikeCount(commentId)
        return ResultEntity.success("查询成功", data = result)
    }
    // 点赞
    @PostMapping("/like")
    fun likeComment(commentId: Int,request: HttpServletRequest) : Any {
        if(!ValidUtils.isValidId(commentId)) {
            return ControllerUtils.INVALID_RESPONSE
        }
        val uid = ControllerUtils.myUserInfo(request).id
        val result = commentLikeService.like(uid, commentId)
        return if(result) {
            ResultEntity.success("点赞成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    // 取消点赞
    @DeleteMapping("/unlike")
    fun unlikeComment(commentId: Int,request: HttpServletRequest) : Any {
        if(!ValidUtils.isValidId(commentId)) {
            return ControllerUtils.INVALID_RESPONSE
        }
        val uid = ControllerUtils.myUserInfo(request).id
        val result = commentLikeService.unlike(uid, commentId)
        return if(result) {
            ResultEntity.success("取消点赞成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
}