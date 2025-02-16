package com.chiuxah.blog.service

import com.chiuxah.blog.config.response.ResponseEntity
import com.chiuxah.blog.mapper.FollowMapper
import com.chiuxah.blog.model.bean.UserInfoSummary
import com.chiuxah.blog.config.response.StatusCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FollowService {
    @Autowired
    lateinit var followMapper: FollowMapper

    // 获取粉丝数量
    fun getFollowersCount(followeeId: Int): Int {
        return followMapper.getFollowersCount(followeeId)
    }
    // 获取关注数量
    fun getFollowingCount(followerId: Int): Int {
        return followMapper.getFollowingCount(followerId)
    }
    // 获取粉丝列表
    fun getFollowersList(followeeId: Int): List<UserInfoSummary> {
        return followMapper.getFollowersList(followeeId)
    }
    // 获取关注列表
    fun getFollowingList(followerId: Int): List<UserInfoSummary> {
        return followMapper.getFollowingList(followerId)
    }
    // 关注某个用户
    fun follow(followerId: Int, followeeId: Int): Boolean {
        return followMapper.follow(followerId, followeeId)
    }
    // 取关某个用户
    fun unfollow(followerId: Int, followeeId: Int): Boolean {
        return followMapper.unfollow(followerId, followeeId)
    }
    // 自己关注自己
    fun isSameUser(left : Int,right : Int) : Pair<Boolean,Any?> {
        return if(left == right) {
            Pair(true, ResponseEntity.fail(StatusCode.BAD_REQUEST,"不能自己关注自己"))
        } else {
            Pair(false,null)
        }
    }
}
