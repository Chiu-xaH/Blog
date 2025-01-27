package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.UserMapper
import com.chiuxah.blog.model.UserInfo
import org.apache.catalina.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired lateinit var userMapper : UserMapper
    // 注册
    fun reg(username : String,password : String) : Int {
        return userMapper.reg(username, password)
    }
    // 登录验证
    fun selectByUsername(username: String) : UserInfo? {
        return userMapper.selectByUsername(username)
    }
    // 用UID查询信息
    fun selectByUid(uid : Int) : UserInfo? {
        return userMapper.selectByUid(uid)
    }
    // 是否注册过
    fun hasAccount(username : String) : Boolean {
        return userMapper.hasAccount(username)
    }
}