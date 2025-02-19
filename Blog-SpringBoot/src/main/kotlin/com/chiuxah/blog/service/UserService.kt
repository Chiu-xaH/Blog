package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.UserMapper
import com.chiuxah.blog.model.bean.UserBean
import com.chiuxah.blog.model.bean.UserSessionSummary
import com.chiuxah.blog.model.bean.UserInfoDTO
import com.chiuxah.blog.model.enums.type.SexType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var userMapper : UserMapper
    // 注册
    fun reg(email: String,username : String, password : String): Boolean {
        return userMapper.reg(email,username, password)
    }
    // 登录验证
    fun selectByEmail(email : String) : UserSessionSummary? {
        return userMapper.selectByEmail(email)
    }
    // 用UID查询信息
    fun selectByUid(uid : Int) : UserBean? {
        return userMapper.selectByUid(uid)
    }
    // 是否注册过
    fun hasAccount(email : String) : Boolean {
        return userMapper.hasAccount(email)
    }

    fun isUserNameExist(username: String): Boolean {
        return userMapper.isUserNameExist(username)
    }

    fun update(userBean: MutableMap<String, Any>) : Boolean {
        return userMapper.update(userBean)
    }
    fun del(uid : Int) : Boolean {
        return userMapper.del(uid)
    }
    // 转换
    fun convertToUserInfoDTO(userInfo: UserBean): UserInfoDTO {
        return UserInfoDTO(
            id = userInfo.id,
            create_time = userInfo.create_time,
            username = userInfo.username,
            email = userInfo.email,
            phone_number = userInfo.phone_number,
            description = userInfo.description,
            sex = when(userInfo.sex) {
                SexType.DEFAULT.type -> SexType.DEFAULT.name
                SexType.MALE.type -> SexType.MALE.name
                SexType.FEMALE.type -> SexType.FEMALE.name
                else -> ""
            },
            born_date = userInfo.born_date,
            region = userInfo.region,
            website = userInfo.website,
            photo = userInfo.photo,
            state = userInfo.state
        )
    }
}