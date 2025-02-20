package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.entity.ImageEntity
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface ImageMapper {
    @Insert("INSERT INTO image_info(url, filename, size, filetype, uid, type, state) " +
            "VALUES(#{url}, #{filename}, #{size}, #{filetype}, #{uid}, #{type}, #{state})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 返回id
    fun add(imageInfo: ImageEntity): Int

    @Select("SELECT * FROM image_info WHERE uid = #{uid}")
    fun selectByUid(uid : Int) : List<ImageEntity>

    @Select("SELECT * FROM image_info WHERE id = #{id}")
    fun selectById(id : Int) : ImageEntity?

    @Select("SELECT * FROM image_info WHERE filename = #{filename}")
    fun selectByFilename(filename : String) : ImageEntity?

    @Update("UPDATE userinfo SET photo = #{url} WHERE id = #{id}")
    fun updateUserPhoto(id : Int,url : String) : Boolean

    @Delete("DELETE FROM image_info WHERE id = #{id}")
    fun delById(id : Int) : Boolean

    @Delete("DELETE FROM image_info WHERE filename = #{filename}")
    fun delByFilename(filename : String) : Boolean
}