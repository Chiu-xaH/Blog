package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.UserInfo
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    // 注册 INSERT INTO userinfo(username, password)
    @Insert("INSERT INTO userinfo (username, password) VALUES (#{username}, #{password})")
    fun reg(
        @Param("username") username : String,
        @Param("password") password : String,
    ) : Int
    // 查询用户 SELECT * FROM userinfo WHERE username = username
    @Select("SELECT * FROM userinfo WHERE username = #{username}")
    fun selectByUsername(
        @Param("username") username : String
    ) : UserInfo?
    // 按ID找 SELECT * FROM userinfo WHERE id = uid
    @Select("SELECT * FROM userinfo WHERE id = #{uid}")
    fun selectByUid(
        @Param("uid") uid : Long
    ) : UserInfo?
    // 验证是否注册过
    @Select("SELECT COUNT(1) FROM userinfo WHERE username = #{username}")
    fun hasAccount(@Param("username") username: String) : Boolean
}