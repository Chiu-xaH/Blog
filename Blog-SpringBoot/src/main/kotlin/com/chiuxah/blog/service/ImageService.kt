package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.ImageMapper
import com.chiuxah.blog.model.entity.ImageEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ImageService {
    @Autowired
    lateinit var imageMapper : ImageMapper

    fun addImage(imageInfo : ImageEntity) : Int {
        return imageMapper.add(imageInfo)
    }

    fun selectByUid(uid : Int) : List<ImageEntity> {
        return imageMapper.selectByUid(uid)
    }

    fun updateUserPhoto(id : Int,url : String) : Boolean {
        return imageMapper.updateUserPhoto(id, url)
    }
    fun delById(id: Int) : Boolean {
        return imageMapper.delById(id)
    }

    fun delByFilename(filename : String) : Boolean {
        return imageMapper.delByFilename(filename)
    }

    fun selectById(id : Int) : ImageEntity? {
        return imageMapper.selectById(id)
    }

    fun selectByFilename(filename : String) : ImageEntity? {
        return imageMapper.selectByFilename(filename)
    }
}