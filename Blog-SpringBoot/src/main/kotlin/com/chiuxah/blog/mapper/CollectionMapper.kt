package com.chiuxah.blog.mapper

import com.chiuxah.blog.model.bean.CollectionInfoSummary
import com.chiuxah.blog.model.entity.collection.CollectionEntity
import com.chiuxah.blog.model.entity.collection.CollectionsFolderEntity
import org.apache.ibatis.annotations.*

@Mapper
interface CollectionMapper {
    // 获取某个收藏夹的信息
    @Select("SELECT * FROM collection_folder WHERE id = #{folderId}")
    fun getFolderInfo(folderId : Int) : CollectionsFolderEntity?
    // 获取某个收藏的信息
    @Select("SELECT * FROM user_collection WHERE id = #{collectionId}")
    fun getCollectionInfo(collectionId: Int) : CollectionEntity?
    // 获取某个收藏夹里面的博客数量
    @Select("SELECT COUNT(*) FROM user_collection WHERE folder_id = #{folderId}")
    fun getFolderCollectionsCount(folderId : Int) : Int
    // 获取某个收藏夹里面博客的列表
    @Select("SELECT id, article_id, folder_id FROM user_collection WHERE folder_id = #{folderId}")
    fun getFolderCollectionsList(folderId : Int) : List<CollectionInfoSummary>
    // 获取某个用户的收藏夹列表
    @Select("SELECT * FROM collection_folder WHERE uid = #{uid}")
    fun getFoldersList(uid : Int) : List<CollectionsFolderEntity>
    // 加入收藏
    @Insert("INSERT INTO user_collection (uid, article_id, folder_id) VALUES (#{uid}, #{articleId}, #{folderId})")
    fun collect(uid: Int,articleId : Int,folderId: Int?) : Boolean
    // 取消收藏
    @Delete("DELETE FROM user_collection WHERE id = #{collectionId} AND uid = #{uid}")
    fun uncollect(collectionId : Int,uid : Int) : Boolean
    // 新建收藏夹
    @Insert("INSERT INTO collection_folder (uid, name, description, state) VALUES (#{uid}, #{name}, #{description}, #{state})")
    fun createFolder(folderInfo : CollectionsFolderEntity) : Boolean
    // 删除收藏夹，并清空里面的收藏内容
    @Delete("DELETE FROM collection_folder WHERE id = #{folderId}")
    fun deleteFolder(folderId: Int) : Boolean
    @Delete("DELETE FROM user_collection WHERE folder_id = #{folderId}")
    fun deleteFolderCollections(folderId: Int) : Boolean
    // 移动博客到某收藏夹(修改收藏项目对应的folder_id即可)
    @Update("UPDATE user_collection SET folder_id = #{newFolderId} WHERE id = #{collectionId}")
    fun moveToFolder(newFolderId : Int,collectionId : Int) : Boolean
    // 获取全部收藏数目
    @Select("SELECT COUNT(*) FROM user_collection WHERE uid = #{uid}")
    fun getAllCollectionsCount(uid : Int) : Int
    // 获取全部收藏的博客列表
    @Select("SELECT id,article_id,folder_id FROM user_collection WHERE uid = #{uid}")
    fun getAllCollectionsList(uid : Int) : List<CollectionInfoSummary>
    // 修改收藏夹的状态
    @Update("UPDATE collection_folder SET state = #{newState} WHERE id = #{folderId}")
    fun changeFolderState(folderId: Int,newState : Int) : Boolean
    // 获取某个文章的收藏数目
    @Select("SELECT COUNT(*) FROM user_collection WHERE article_id = #{articleId}")
    fun getArticleCollectionsCount(articleId : Int) : Int
}