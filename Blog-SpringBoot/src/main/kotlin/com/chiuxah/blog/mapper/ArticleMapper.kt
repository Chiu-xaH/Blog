package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.ArticleInfo
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface ArticleMapper {
    // 发布新文章
    @Insert("INSERT INTO articleinfo(id, title, content, uid, rcount, state) VALUES(#{articleInfo.id}, #{articleInfo.title}, #{articleInfo.content}, #{articleInfo.uid}, #{articleInfo.rcount}, #{articleInfo.state})")
    fun add(@Param("articleInfo") articleInfo: ArticleInfo) : Int
    // 查询指定博客id的详情
    @Select("SELECT * FROM articleinfo WHERE id = #{blogId}")
    fun selectByBlogId(@Param("blogId") blogId : Int) : ArticleInfo?
    // 查询个人博客列表
    @Select("SELECT * FROM articleinfo WHERE uid = #{uid}")
    fun getMyBlogList(@Param("uid") uid : Int) : List<ArticleInfo>
    // 查询总的博客列表
    @Select("SELECT * FROM articleinfo")
    fun getBlogList() : List<ArticleInfo>
    // 删除指定博客
    @Delete("DELETE FROM articleinfo WHERE id = #{id}")
    fun del(@Param("id") id : Int) : Int
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
    @Update("UPDATE articleinfo SET title = #{title}, content = #{content} WHERE id = #{id}")
    fun update(
        @Param("title") title : String,
        @Param("content") content : String
    ) : Int
}