package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.bean.CommentBean
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface CommentMapper {
    // 根据id获取评论内容
    @Select("SELECT * FROM comment WHERE id = #{commentId}")
    fun getCommentInfo(commentId: Int) : CommentBean?
    // 获取文章的评论们 // 按时间排序
    // 获取评论的子评论们 // 按时间排序
    @Select("""
        SELECT * FROM comment 
        WHERE (article_id = #{articleId} AND #{commentId} IS NULL) 
           OR (parent_comment_id = #{commentId} AND #{articleId} IS NULL) 
        ORDER BY create_time ASC
    """)
    fun getComments(articleId: Int?, commentId: Int?): List<CommentBean>
    // 评论
    @Insert("INSERT INTO comment (uid, article_id, parent_comment_id, content, image_url) VALUES (#{uid}, #{article_id}, #{parent_comment_id}, #{content}, #{image_url})")
    fun add(comment : CommentBean) : Boolean
    // 删评
    @Delete("DELETE FROM comment WHERE id = #{commentId}")
    fun del(commentId : Int) : Boolean
    // 获取文章总评论数量
    @Select("""
        SELECT COUNT(*) FROM comment 
        WHERE (article_id = #{articleId} AND #{commentId} IS NULL) 
        OR (parent_comment_id = #{commentId} AND #{articleId} IS NULL)
    """)
    fun getCommentCount(articleId: Int?, commentId: Int?): Int
    // 获取自己的发布评论
    @Select("SELECT * FROM comment WHERE uid = #{uid} ORDER BY create_time DESC")
    fun getUserComments(uid: Int): List<CommentBean>
}