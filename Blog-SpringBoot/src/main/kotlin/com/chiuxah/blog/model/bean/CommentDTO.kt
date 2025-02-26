package com.chiuxah.blog.model.bean

import com.chiuxah.blog.model.entity.CommentEntity

data class CommentDTO(
    val type : String,
    val commentInfo : CommentEntity, // 当前评论的信息
    val children : List<CommentDTO>, // 子评论
)
