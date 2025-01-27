package com.chiuxah.blog.model

import java.sql.Timestamp

data class CommentInfo(
    val id : Long = 0L,
    val uid : Long,
    val articleId : Long,
    val parentId : Long,//如果是顶级评论，parent_id 设置为 0； 如果是子评论，parent_id 指向父评论的 ID，用于构建评论的层级关系。
    val content : String,
    val likecount : Int  = 0,// 点赞次数
    val creattime : Timestamp,
    val updatetime : Timestamp,
    val state : Int
)
