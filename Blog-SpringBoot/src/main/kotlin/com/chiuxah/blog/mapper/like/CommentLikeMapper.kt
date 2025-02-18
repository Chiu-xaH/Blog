package com.chiuxah.blog.mapper.like

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface CommentLikeMapper {
    // 查询某条评论点赞量
    @Select("SELECT COUNT(*) FROM comment_like WHERE comment_id = #{commentId}")
    fun getLikeCount(commentId : Int) : Int
    // 点赞
    @Insert("INSERT INTO comment_like (uid,comment_id) VALUES (#{uid}, #{commentId})")
    fun like(uid : Int,commentId : Int) : Boolean
    // 取消点赞
    @Delete("DELETE FROM comment_like WHERE uid = #{uid} AND comment_id = #{commentId}")
    fun unlike(uid : Int,commentId : Int) : Boolean
}