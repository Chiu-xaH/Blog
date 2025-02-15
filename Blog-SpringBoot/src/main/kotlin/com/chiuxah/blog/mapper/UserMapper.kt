package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.UserInfo
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    // 注册 INSERT INTO userinfo(username, password)
    @Insert("INSERT INTO user_info (username, password) VALUES (#{username}, #{password})")
    fun reg(username : String, password : String) : Boolean
    // 查询用户 SELECT * FROM userinfo WHERE username = username
    @Select("SELECT * FROM user_info WHERE username = #{username}")
    fun selectByUsername(username : String) : UserInfo?
    // 按ID找 SELECT * FROM userinfo WHERE id = uid
    @Select("SELECT * FROM user_info WHERE id = #{uid}")
    fun selectByUid(uid : Int) : UserInfo?
    // 验证是否注册过
    @Select("SELECT COUNT(1) FROM user_info WHERE username = #{username}")
    fun hasAccount(username: String) : Boolean
}