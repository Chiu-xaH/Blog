package com.chiuxah.blog.model.entity

import com.chiuxah.blog.model.entity.base.BaseSqlEntity
import java.sql.Timestamp

data class CommentEntity(
    override val id : Int = 0,
    override val create_time : Timestamp? = null,
    val uid : Int,
    val article_id : Int?,
    val parent_comment_id : Int?,//如果是顶级评论，parent_id 设置为 0； 如果是子评论，parent_id 指向父评论的 ID，用于构建评论的层级关系。
    val content : String,
    val image_url : String?,
) : BaseSqlEntity()
