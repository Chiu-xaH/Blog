package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResponseEntity
import com.chiuxah.blog.model.bean.UserBean
import com.chiuxah.blog.service.FollowService
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// 粉丝、关注
@RestController
@RequestMapping("/api/v1/follow")
class FollowController {
    @Autowired
    lateinit var followService: FollowService
    // uid的粉丝及其关注汇总
    @GetMapping("/count")
    fun getFollowCount(uid : Int) : Any {
        // uid有多少粉丝
        val followersCount = followService.getFollowersCount(uid)
        // uid关注了多少人
        val followeeCount = followService.getFollowingCount(uid)
        return ResponseEntity.success("查询成功", mapOf(
            "followersCount" to followersCount,
            "followeeCount" to followeeCount
        ))
    }
    // 粉丝列表 私人秘密，只能自己看
    @GetMapping("/follower")
    fun getFollowerList(request: HttpServletRequest) : Any {
        val session = myUserInfo(request)
        val resultList = followService.getFollowersList(session.id)
        return ResponseEntity.success("查询成功",resultList)
    }
    // 关注列表 私人秘密，只能自己看
    @GetMapping("/followee")
    fun getFolloweeList(request: HttpServletRequest) : Any {
        val session = myUserInfo(request)
        val resultList = followService.getFollowingList(session.id)
        return ResponseEntity.success("查询成功",resultList)
    }
    // 关注
    @PostMapping("/follow")
    fun follow(request: HttpServletRequest,uid : Int) : Any {
        val session = myUserInfo(request)
        val left = session.id
        val isSame = followService.isSameUser(uid,left)
        // 我关注我自己
        if(isSame.first) {
            return isSame.second!!
        }
        val result = followService.follow(left,uid)
        return if(result) {
            ResponseEntity.success("关注成功")
        } else {
            ResponseEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"关注失败")
        }
    }
    // 取关
    @DeleteMapping("/unfollow")
    fun unfollow(request: HttpServletRequest,uid : Int) : Any {
        val session = myUserInfo(request)
        val left = session.id
        val isSame = followService.isSameUser(uid,left)
        // 我关注我自己
        if(isSame.first) {
            return isSame.second!!
        }
        val result = followService.unfollow(left,uid)
        return if(result) {
            ResponseEntity.success("取关成功")
        } else {
            ResponseEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"取关失败")
        }
    }
}