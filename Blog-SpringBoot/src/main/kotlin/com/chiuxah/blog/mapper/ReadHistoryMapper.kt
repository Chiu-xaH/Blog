package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.bean.ReadHistoryInfoSummary
import com.chiuxah.blog.model.entity.ReadHistoryEntity
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface ReadHistoryMapper {
    // 按时间顺序给出用户访问记录
    @Select("SELECT * FROM article_read_history WHERE uid = #{uid} ORDER BY last_view_time DESC")
    fun getUserReadHistories(uid: Int) : List<ReadHistoryEntity>
    // 查看文章有谁看了（时间顺序）
    @Select("SELECT uid,last_view_time FROM article_read_history WHERE article_id = #{articleId} ORDER BY last_view_time DESC")
    fun getReadUsers(articleId: Int) : List<ReadHistoryInfoSummary>
    // 文章阅读量
    @Select("SELECT COUNT(*) FROM article_read_history WHERE article_id = #{articleId}")
    fun getReadCount(articleId : Int) : Int
    // 用户新阅读了某文章
    @Insert(
        """
        INSERT INTO article_read_history (uid, article_id) VALUES (#{uid}, #{articleId})
        ON DUPLICATE KEY UPDATE last_view_time = NOW()
        """
    )
    fun read(uid : Int,articleId: Int) : Boolean
}