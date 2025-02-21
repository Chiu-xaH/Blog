package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.VisitMapper
import com.chiuxah.blog.model.bean.VisitInfoSummary
import com.chiuxah.blog.model.entity.VisitEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class VisitService {
    @Autowired
    lateinit var visitMapper: VisitMapper
    // 按时间顺序给出用户访问记录
    fun getUserReadHistories(uid: Int) : List<VisitEntity> {
        return visitMapper.getUserReadHistories(uid)
    }
    // 查看文章有谁看了（时间顺序）
    fun getReadUsers(articleId: Int) : List<VisitInfoSummary> {
        return visitMapper.getReadUsers(articleId)
    }
    // 文章阅读量
    fun getReadCount(articleId : Int) : Int {
        return visitMapper.getReadCount(articleId)
    }
    // 用户阅读了某文章
    fun read(uid : Int,articleId: Int) : Boolean {
        return visitMapper.read(uid, articleId)
    }
}