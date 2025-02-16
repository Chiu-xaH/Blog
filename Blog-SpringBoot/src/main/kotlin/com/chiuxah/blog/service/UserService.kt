package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.UserMapper
import com.chiuxah.blog.model.bean.UserBean
import com.chiuxah.blog.model.bean.UserInfoDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired lateinit var userMapper : UserMapper
    // 注册
    fun reg(username : String,password : String) : Boolean {
        return userMapper.reg(username, password)
    }
    // 登录验证
    fun selectByUsername(username: String) : UserBean? {
        return userMapper.selectByUsername(username)
    }
    // 用UID查询信息
    fun selectByUid(uid : Int) : UserBean? {
        return userMapper.selectByUid(uid)
    }
    // 是否注册过
    fun hasAccount(username : String) : Boolean {
        return userMapper.hasAccount(username)
    }
    // 转换
    fun convertToUserInfoDTO(userInfo: UserBean): UserInfoDTO {
        return UserInfoDTO(
            id = userInfo.id,
            username = userInfo.username,
            photo = userInfo.photo,
            create_time = userInfo.create_time,
            state = userInfo.state
        )
    }
}