package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.ImageMapper
import com.chiuxah.blog.model.ImageInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageService {
    @Autowired lateinit var imageMapper : ImageMapper

    fun addImage(imageInfo : ImageInfo) : Int {
        return imageMapper.add(imageInfo)
    }

    fun selectByUid(uid : Int) : List<ImageInfo> {
        return imageMapper.selectByUid(uid)
    }

    fun updateUserPhoto(id : Int,url : String) : Int {
        return imageMapper.updateUserPhoto(id, url)
    }
    fun delImage(id: Int) : Int {
        return imageMapper.del(id)
    }
}