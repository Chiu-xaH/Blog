package com.chiuxah.blog.service.like

import com.chiuxah.blog.mapper.like.ArticleLikeMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleLikeService {
    @Autowired
    lateinit var articleLikeMapper: ArticleLikeMapper

    fun getLikeCount(articleId : Int) : Int {
        return articleLikeMapper.getLikeCount(articleId)
    }

    fun like(uid : Int,articleId : Int) : Boolean {
        return articleLikeMapper.like(uid, articleId)
    }

    fun unlike(uid : Int,articleId : Int) : Boolean {
        return articleLikeMapper.unlike(uid, articleId)
    }

    fun getLikeList(articleId : Int) : List<Int> {
        return articleLikeMapper.getLikeList(articleId)
    }
}