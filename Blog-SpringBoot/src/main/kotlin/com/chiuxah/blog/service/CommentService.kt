package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.CommentMapper
import com.chiuxah.blog.model.entity.CommentEntity
import com.chiuxah.blog.model.enums.type.ParentCommentType
import com.chiuxah.blog.utils.ValidUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {
    @Autowired
    lateinit var commentMapper: CommentMapper
    // 获取单条评论信息
    fun getCommentInfo(commentId: Int): CommentEntity? {
        return commentMapper.getCommentInfo(commentId)
    }
    // 获取文章的所有评论
    fun getComments(type : ParentCommentType,articleId: Int?,commentId: Int?): List<CommentEntity> {
        return when(type) {
            ParentCommentType.COMMENT -> commentMapper.getComments(articleId = null, commentId = commentId)
            ParentCommentType.ARTICLE -> commentMapper.getComments(articleId = articleId, commentId = null)
        }
    }
    // 发布评论
    fun add(comment: CommentEntity): Boolean {
        return commentMapper.add(comment)
    }
    // 删除评论
    fun del(commentId: Int): Boolean {
        return commentMapper.del(commentId)
    }
    // 获取文章的评论总数
    fun getCommentCount(type : ParentCommentType,articleId: Int?,commentId: Int?): Int {
        return when(type) {
            ParentCommentType.COMMENT -> commentMapper.getCommentCount(articleId = null, commentId = commentId)
            ParentCommentType.ARTICLE -> commentMapper.getCommentCount(articleId = articleId, commentId = null)
        }
    }
    // 获取某用户的所有评论
    fun getUserComments(uid: Int): List<CommentEntity> {
        return commentMapper.getUserComments(uid)
    }
    // 合理性判断
    fun parentType(articleId: Int?,commentId: Int?) : ParentCommentType? {
        return if(articleId != null && commentId == null) {
            if(!ValidUtils.isValidId(articleId)) {
                null
            } else {
                ParentCommentType.ARTICLE
            }
        } else if(articleId == null && commentId != null) {
            if(!ValidUtils.isValidId(commentId)) {
                null
            } else {
                ParentCommentType.COMMENT
            }
        } else {
            null
        }
    }
}
