package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.ArticleMapper
import com.chiuxah.blog.model.bean.ArticleBean
import com.chiuxah.blog.model.bean.ArticleInfoSummary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService {
    @Autowired lateinit var articleMapper : ArticleMapper

    // 发布新文章
    fun add(articleInfo: ArticleBean) : Boolean {
        return articleMapper.add(articleInfo)
    }
    // 查询指定博客id的详情
    fun selectByBlogId(blogId : Int) : ArticleBean? {
        return articleMapper.selectByBlogId(blogId)
    }
    // 查询个人博客列表
    fun getBlogListByUser(uid : Int) : List<ArticleInfoSummary> {
        return articleMapper.getMyBlogList(uid)
    }
    // 查询总的博客列表
    fun getBlogList() : List<ArticleInfoSummary> {
        return articleMapper.getBlogList()
    }
    // 删除指定博客
    fun del(id : Int) : Boolean {
        return  articleMapper.del(id)
    }
    // 当前博客总数目
    fun getPageCount() : Int {
        return articleMapper.getPageCount()
    }
    // 查询总的博客列表(分页)
    fun getBlogListByPage(pageSize : Int = 15, page : Int = 1) : List<ArticleInfoSummary> {
        return articleMapper.getBlogListByPage(pageSize, page)
    }
    // 修改博客title和content
    fun update(id : Int,title : String?, content : String?) : Boolean {
        return articleMapper.update(id,title, content)
    }
}