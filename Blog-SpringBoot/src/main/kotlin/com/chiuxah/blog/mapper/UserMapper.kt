package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.bean.UserSessionSummary
import com.chiuxah.blog.model.entity.UserEntity
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface UserMapper {
    // 注册
    @Insert("INSERT INTO user_info (email, username, password) VALUES (#{email}, #{username}, #{password})")
    fun reg(email: String,username : String, password : String) : Boolean
    // 注销
    @Delete("DELETE FROM user_info WHERE id = #{uid}")
    fun del(uid : Int) : Boolean
    // 修改用户信息
    @Update("""
        <script>
            UPDATE user_info
            <set>
                <if test="username != null">username = #{username},</if>
                <if test="password != null">password = #{password},</if>
                <if test="email != null">email = #{email},</if>
                <if test="phone_number != null">phone_number = #{phone_number},</if>
                <if test="description != null">description = #{description},</if>
                <if test="sex != null">sex = #{sex},</if>
                <if test="born_date != null">born_date = #{born_date},</if>
                <if test="region != null">region = #{region},</if>
                <if test="website != null">website = #{website},</if>
                <if test="photo != null">photo = #{photo}</if>
            </set>
            WHERE id = #{id}
        </script>
    """)
    fun update(userBean: MutableMap<String, Any>) : Boolean
    // 查询用户 用于验证身份
    @Select("SELECT id,username,email,photo,password FROM user_info WHERE email = #{email}")
    fun selectByEmail(email : String) : UserSessionSummary?
    // 按ID找 用于用户信息
    @Select("SELECT * FROM user_info WHERE id = #{uid}")
    fun selectByUid(uid : Int) : UserEntity?
    // 验证是否注册过
    @Select("SELECT COUNT(1) FROM user_info WHERE email = #{email}")
    fun hasAccount(email: String) : Boolean
    // 验证是否昵称被占用
    @Select("SELECT COUNT(1) FROM user_info WHERE username = #{username}")
    fun isUserNameExist(username: String): Boolean
    // 清空文章

}