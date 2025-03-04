package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.bean.ArticleInfoSummary
import com.chiuxah.blog.model.entity.ArticleEntity
import org.apache.ibatis.annotations.*

@Mapper
interface ArticleMapper {
    // 发布新文章
    @Insert("INSERT INTO article_info(id, title, content, uid) VALUES(#{id}, #{title}, #{content}, #{uid})")
    fun add(articleInfo: ArticleEntity) : Boolean
    // 查询指定博客id的详情
    @Select("SELECT * FROM article_info WHERE id = #{blogId}")
    fun getArticleInfo(blogId : Int) : ArticleEntity?
    // 查询个人博客列表 根据更新时间
    @Select("SELECT id,title,update_time,uid,rcount FROM article_info WHERE uid = #{uid} ORDER BY update_time DESC")
    fun getUserArticles(uid : Int) : List<ArticleInfoSummary>
    // 查询总的博客列表
    @Select("SELECT id,title,update_time,uid FROM article_info ORDER BY update_time DESC")
    fun getAllArticles() : List<ArticleInfoSummary>
    // 删除指定博客
    @Delete("DELETE FROM article_info WHERE id = #{id}")
    fun del(id : Int) : Boolean
    // 当前博客总数目
    @Select("SELECT COUNT(*) FROM article_info")
    fun getPageCount() : Int
    // 查询总的博客列表(分页)
    @Select("""
        SELECT id, title, update_time, uid 
        FROM article_info 
        ORDER BY update_time DESC 
        LIMIT #{limit} OFFSET #{offset}
    """)
    fun getAllArticlesByPage(
        @Param("limit") pageSize : Int,
        @Param("offset") page : Int
    ) : List<ArticleInfoSummary>
    // 修改博客title和content
    @Update(
        "<script>" +
        "UPDATE article_info" +
        "<set>" +
        "<if test='title != null and title != \"\"'>title = #{title},</if>" +
        "<if test='content != null and content != \"\"'>content = #{content},</if>" +
        "</set>" +
        "WHERE id = #{id}" +
        "</script>"
    )
    fun update(
        id: Int,
        title: String?,
        content: String?
    ): Boolean
}