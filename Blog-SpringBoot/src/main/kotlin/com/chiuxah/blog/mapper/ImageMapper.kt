package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.ImageInfo
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface ImageMapper {
    @Insert("INSERT INTO imageinfo(url, filename, size, filetype, uid, type, state) " +
            "VALUES(#{url}, #{filename}, #{size}, #{filetype}, #{uid}, #{type}, #{state})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 返回id
    fun add(imageInfo: ImageInfo): Int

    @Select("SELECT * FROM imageinfo WHERE uid = #{uid}")
    fun selectByUid(uid : Int) : List<ImageInfo>

    @Update("UPDATE userinfo SET photo = #{url} WHERE id = #{id}")
    fun updateUserPhoto(id : Int,url : String) : Int

    @Delete("DELETE imageinfo WHERE id = #{id}")
    fun del(id : Int) : Int
}