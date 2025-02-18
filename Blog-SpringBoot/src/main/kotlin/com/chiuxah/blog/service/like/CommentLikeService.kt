package com.chiuxah.blog.service.like

import com.chiuxah.blog.mapper.like.CommentLikeMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentLikeService {
    @Autowired
    lateinit var commentLikeMapper: CommentLikeMapper

    fun getLikeCount(commentId : Int) : Int {
        return commentLikeMapper.getLikeCount(commentId)
    }

    fun like(uid : Int,commentId : Int) : Boolean {
        return commentLikeMapper.like(uid, commentId)
    }

    fun unlike(uid : Int,commentId : Int) : Boolean {
        return commentLikeMapper.unlike(uid, commentId)
    }
}