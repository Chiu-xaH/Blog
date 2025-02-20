package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.ReadHistoryMapper
import com.chiuxah.blog.model.bean.ReadHistoryInfoSummary
import com.chiuxah.blog.model.entity.ReadHistoryEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReadHistoryService {
    @Autowired
    lateinit var readHistoryMapper: ReadHistoryMapper
    // 按时间顺序给出用户访问记录
    fun getUserReadHistories(uid: Int) : List<ReadHistoryEntity> {
        return readHistoryMapper.getUserReadHistories(uid)
    }
    // 查看文章有谁看了（时间顺序）
    fun getReadUsers(articleId: Int) : List<ReadHistoryInfoSummary> {
        return readHistoryMapper.getReadUsers(articleId)
    }
    // 文章阅读量
    fun getReadCount(articleId : Int) : Int {
        return readHistoryMapper.getReadCount(articleId)
    }
    // 用户阅读了某文章
    fun read(uid : Int,articleId: Int) : Boolean {
        return readHistoryMapper.read(uid, articleId)
    }
}