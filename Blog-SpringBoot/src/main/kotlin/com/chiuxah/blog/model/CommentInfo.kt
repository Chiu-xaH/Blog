package com.chiuxah.blog.model

import com.chiuxah.blog.model.base.BaseSqlBean
import java.sql.Timestamp

data class CommentInfo(
    override val id : Int = 0,
    val uid : Long,
    val articleId : Long,
    val parentId : Long,//如果是顶级评论，parent_id 设置为 0； 如果是子评论，parent_id 指向父评论的 ID，用于构建评论的层级关系。
    val content : String,
    val likecount : Int  = 0,// 点赞次数
    override val create_time : Timestamp? = null,
    val updatetime : Timestamp,
    val state : Int
) : BaseSqlBean()
