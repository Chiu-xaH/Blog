package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.model.entity.CommentEntity
import com.chiuxah.blog.model.enums.type.ImageType
import com.chiuxah.blog.service.CommentService
import com.chiuxah.blog.utils.ControllerUtils.CONTROLLER_ERROR_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.DATABASE_ERROR_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.EMPTY_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.USER_FORBID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.jsonToMap
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import com.chiuxah.blog.utils.ValidUtils.isValidId
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

// 评论模块 评论评论 评论文章
@RestController
@RequestMapping("/api/v1/comment")
class CommentController {
    @Autowired
    lateinit var commentService : CommentService
    @Autowired
    lateinit var imageController: ImageController
    // 获取评论内容
    @GetMapping("/info")
    fun getCommentInfo(commentId : Int) : Any {
        if(!isValidId(commentId)) {
            return INVALID_RESPONSE
        }
        val result = commentService.getCommentInfo(commentId)
        return if(result == null) {
            EMPTY_RESPONSE
        } else {
            ResultEntity.success("查找成功", data = result)
        }
    }
    // 获取评论
    @GetMapping("/all")
    fun getArticleComments(articleId : Int?,commentId: Int?) : Any {
        val type = commentService.parentType(articleId, commentId) ?: return INVALID_RESPONSE
        val result = commentService.getComments(type,articleId,commentId)
        return ResultEntity.success("获取成功", data = result)
    }
    // 评论
    @PostMapping("/add")
    fun addComment(articleId : Int?, commentId: Int?, content : String, image : MultipartFile?,request: HttpServletRequest) : Any {
        // 参数合理性判断
        val type = commentService.parentType(articleId, commentId) ?: return INVALID_RESPONSE
        // 调用图床模块
        val url = if(image == null) {
            null
        } else {
            val imageResponse = imageController.uploadImage(image,type = ImageType.COMMENT_PHOTO.name,request)
            if(!isSuccessResponse(imageResponse)) {
                return CONTROLLER_ERROR_RESPONSE
            }
            val data = jsonToMap(imageResponse)["data"] as Map<*,*>
            data["url"] as String
        }
        // 数据组装
        val comment = CommentEntity(
            uid = myUserInfo(request).id,
            article_id = articleId,
            parent_comment_id = commentId,
            content = content,
            image_url = url
        )
        // 插入数据库
        val result = commentService.add(comment)
        return if(result) {
            ResultEntity.success("评论成功", data = type.name)
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    // 删评
    @DeleteMapping("/del")
    fun delComment(request: HttpServletRequest,commentId: Int) : Any {
        if(!isValidId(commentId)) {
            return INVALID_RESPONSE
        }
        val uid = myUserInfo(request).id
        val commentResponse = getCommentInfo(commentId)
        if(!isSuccessResponse(commentResponse)) {
            return CONTROLLER_ERROR_RESPONSE
        }
        // 发评论的人才可以删除
        val commentInfo = jsonToMap(commentResponse)["data"] as CommentEntity
        if(commentInfo.uid != uid) {
            return USER_FORBID_RESPONSE
        }
        // 删除
        val result = commentService.del(commentId)
        return if(result) {
            ResultEntity.success("删除成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    // 获取评论数量
    @GetMapping("/count")
    fun getCommentCount(articleId : Int? = null,commentId: Int? = null) : Any {
        val type = commentService.parentType(articleId, commentId) ?: return INVALID_RESPONSE
        val result = commentService.getCommentCount(type,articleId,commentId)
        return ResultEntity.success("查询成功", data = result)
    }
    // 获取自己发布的评论
    @GetMapping("/mine")
    fun getMyComments(request: HttpServletRequest) : Any {
        val uid = myUserInfo(request).id
        val result = commentService.getUserComments(uid)
        return ResultEntity.success("查询成功", data = result)
    }
}