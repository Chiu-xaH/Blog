package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.ImageInfo
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options

@Mapper
interface ImageMapper {
    @Insert("INSERT INTO imageinfo(url, filename, size, filetype, uid, type, state) " +
            "VALUES(#{url}, #{filename}, #{size}, #{filetype}, #{uid}, #{type}, #{state})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 返回id
    fun add(imageInfo: ImageInfo): Int
}