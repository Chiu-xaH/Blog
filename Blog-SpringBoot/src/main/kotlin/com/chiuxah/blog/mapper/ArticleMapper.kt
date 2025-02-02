package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.ArticleInfo
import org.apache.ibatis.annotations.*

@Mapper
interface ArticleMapper {
    // 发布新文章
    @Insert("INSERT INTO articleinfo(id, title, content, uid) VALUES(#{id}, #{title}, #{content}, #{uid})")
    fun add(articleInfo: ArticleInfo) : Int
    // 查询指定博客id的详情
    @Select("SELECT * FROM articleinfo WHERE id = #{blogId}")
    fun selectByBlogId(blogId : Int) : ArticleInfo?
    // 查询个人博客列表
    @Select("SELECT * FROM articleinfo WHERE uid = #{uid}")
    fun getMyBlogList(uid : Int) : List<ArticleInfo>
    // 查询总的博客列表
    @Select("SELECT * FROM articleinfo")
    fun getBlogList() : List<ArticleInfo>
    // 删除指定博客
    @Delete("DELETE FROM articleinfo WHERE id = #{id}")
    fun del(id : Int) : Int
    // 当前博客总数目
    @Select(" SELECT COUNT(*) FROM articleinfo")
    fun getPageCount() : Int
    // 查询总的博客列表(分页)
    @Select("SELECT * FROM articleinfo limit #{limit} offset #{offset}")
    fun getBlogListByPage(
        @Param("limit") pageSize : Int = 15,
        @Param("offset") page : Int = 1
    ) : List<ArticleInfo>
    // 修改博客title和content
    @Update(
        "<script>" +
        "UPDATE articleinfo" +
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
    ): Int
}