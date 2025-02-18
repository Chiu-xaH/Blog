package com.chiuxah.blog.mapper.like

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface ArticleLikeMapper {
    // 查询某个文章点赞量
    @Select("SELECT COUNT(*) FROM article_like WHERE article_id = #{articleId}")
    fun getLikeCount(articleId : Int) : Int
    // 点赞
    @Insert("INSERT INTO article_like (uid,article_id) VALUES (#{uid}, #{articleId})")
    fun like(uid : Int,articleId : Int) : Boolean
    // 取消点赞
    @Delete("DELETE FROM article_like WHERE uid = #{uid} AND article_id = #{articleId}")
    fun unlike(uid : Int,articleId : Int) : Boolean
    // 查询谁给文章点赞了
    @Select("SELECT uid FROM article_like WHERE article_id = #{articleId}")
    fun getLikeList(articleId : Int) : List<Int>
}