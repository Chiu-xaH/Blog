package com.chiuxah.blog.service

import com.chiuxah.blog.mapper.CollectionMapper
import com.chiuxah.blog.model.bean.collection.CollectionBean
import com.chiuxah.blog.model.bean.collection.CollectionSummaryInfo
import com.chiuxah.blog.model.bean.collection.CollectionsFolderBean
import com.chiuxah.blog.model.enums.CollectionsFolderState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CollectionService {
    @Autowired lateinit var collectionMapper: CollectionMapper

    // 获取某个收藏夹的信息
    fun getFolderInfo(folderId: Int): CollectionsFolderBean? {
        return collectionMapper.getFolderInfo(folderId)
    }

    // 获取某个收藏的信息
    fun getCollectionInfo(collectionId: Int): CollectionBean? {
        return collectionMapper.getCollectionInfo(collectionId)
    }

    // 获取某个收藏夹里的博客数量
    fun getFolderCollectionsCount(folderId: Int): Int {
        return collectionMapper.getFolderCollectionsCount(folderId)
    }

    // 获取某个收藏夹里的博客列表
    fun getFolderCollectionsList(folderId: Int): List<CollectionSummaryInfo> {
        return collectionMapper.getFolderCollectionsList(folderId)
    }

    // 获取某个用户的收藏夹列表
    fun getFoldersList(uid: Int): List<CollectionsFolderBean> {
        return collectionMapper.getFoldersList(uid)
    }

    // 加入收藏
    fun collect(collection: CollectionBean): Boolean {
        return collectionMapper.collect(collection.uid,collection.article_id,collection.folder_id)
    }

    // 取消收藏
    fun uncollect(collectionId: Int,uid : Int): Boolean {
        return collectionMapper.uncollect(collectionId,uid)
    }

    // 新建收藏夹
    fun createFolder(folderInfo: CollectionsFolderBean): Boolean {
        return collectionMapper.createFolder(folderInfo)
    }

    // 删除收藏夹，并清空里面的收藏内容
    fun deleteFolder(folderId: Int): Boolean {
        return collectionMapper.deleteFolder(folderId) && collectionMapper.deleteFolderCollections(folderId)
    }

    // 移动博客到某收藏夹
    fun moveToFolder(newFolderId: Int, collectionId: Int): Boolean {
        return collectionMapper.moveToFolder(newFolderId, collectionId)
    }

    // 获取某个用户的所有收藏数量
    fun getAllCollectionsCount(uid: Int): Int {
        return collectionMapper.getAllCollectionsCount(uid)
    }

    // 获取某个用户的所有收藏博客列表
    fun getAllCollectionsList(uid: Int): List<CollectionSummaryInfo> {
        return collectionMapper.getAllCollectionsList(uid)
    }

    fun changeFolderState(folderId: Int,newState : CollectionsFolderState) : Boolean {
        return collectionMapper.changeFolderState(folderId, newState.state)
    }
}
