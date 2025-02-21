package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.ArticleMapper
import com.chiuxah.blog.model.bean.ArticleInfoSummary
import com.chiuxah.blog.model.entity.ArticleEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService {
    @Autowired
    lateinit var articleMapper : ArticleMapper

    // 发布新文章
    fun add(articleInfo: ArticleEntity) : Boolean {
        return articleMapper.add(articleInfo)
    }
    // 查询指定博客id的详情
    fun getArticleInfo(articleId : Int) : ArticleEntity? {
        return articleMapper.getArticleInfo(articleId)
    }
    // 查询个人博客列表
    fun getUserArticles(uid : Int) : List<ArticleInfoSummary> {
        return articleMapper.getUserArticles(uid)
    }
    // 查询总的博客列表
    fun getAllArticles() : List<ArticleInfoSummary> {
        return articleMapper.getAllArticles()
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
    fun getAllArticlesByPage(pageSize : Int, page : Int) : List<ArticleInfoSummary> {
        return articleMapper.getAllArticlesByPage(pageSize, page)
    }
    // 修改博客title和content
    fun update(id : Int,title : String?, content : String?) : Boolean {
        return articleMapper.update(id,title, content)
    }
}