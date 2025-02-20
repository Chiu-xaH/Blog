package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.model.entity.collection.CollectionEntity
import com.chiuxah.blog.model.entity.collection.CollectionsFolderEntity
import com.chiuxah.blog.model.enums.state.CollectionsFolderState
import com.chiuxah.blog.service.CollectionService
import com.chiuxah.blog.utils.ControllerUtils.DATABASE_ERROR_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.EMPTY_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.USER_FORBID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.jsonToMap
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import com.chiuxah.blog.utils.ValidUtils.isValidId
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// 收藏夹
@RestController
@RequestMapping("/api/v1/collection")
class CollectionController {
    @Autowired
    lateinit var collectService: CollectionService
//    @GetMapping("/folder/collections-count")
//    fun getFolderCollectionsCount(folderId : Int) : Any {
//
//    }
    @GetMapping("/folder/info")
    fun getFolderInfo(folderId : Int,request: HttpServletRequest) : Any {
        // 检查是否有查看权限
        // 如果是本人，随便看
        // 如果不是本人的，需要检查state
        if(!isValidId(folderId)) return INVALID_RESPONSE
        val uid = myUserInfo(request).id
        val folderInfo = collectService.getFolderInfo(folderId) ?: return EMPTY_RESPONSE
        return if(folderInfo.uid == uid) {
            // 是本人的收藏夹
            ResultEntity.success("查询成功 本人的收藏夹",folderInfo)
        } else {
            if(folderInfo.state == CollectionsFolderState.PRIVATE.state) {
                USER_FORBID_RESPONSE
            } else {
                ResultEntity.success("查询成功 他人的收藏夹",folderInfo)
            }
        }
    }
    @GetMapping("/folder/collection")
    fun getFolderCollectionsList(folderId : Int,request : HttpServletRequest) : Any {
        if(!isValidId(folderId)) return INVALID_RESPONSE
        val responseBody = getFolderInfo(folderId,request)
        return if(isSuccessResponse(responseBody)) {
            val data = jsonToMap(responseBody)["data"] as CollectionsFolderEntity
            val list = collectService.getFolderCollectionsList(folderId)
            ResultEntity.success("查询成功", mapOf(
                "info" to data,
                "items" to list
            ))
        } else {
            // 解析失败
            responseBody
        }
    }
    // 获取某个用户的收藏夹列表
    @GetMapping("/folder/user")
    fun getFolders(uid : Int,request: HttpServletRequest) : Any {
        if(!isValidId(uid)) return INVALID_RESPONSE
        val folders = collectService.getFoldersList(uid)
        // 如果不是本人，筛选隐私收藏夹
        val newList = if(uid != myUserInfo(request).id) {
            folders.filter { item ->
                item.state == CollectionsFolderState.PUBLIC.state
            }
        } else {
            folders
        }
        return ResultEntity.success("获取成功",newList)
    }
    @PostMapping("/folder/create")
    fun createFolder(name: String,description: String? = null,state: String,request: HttpServletRequest) : Any {
        val folderState = toState(state) ?: return INVALID_RESPONSE
        val session = myUserInfo(request)
        val result = collectService.createFolder(
            CollectionsFolderEntity(
                state = folderState.state,
                uid = session.id,
                description = description,
                name = name
            )
        )
        return if(result) {
            ResultEntity.success("创建成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    @DeleteMapping("folder/del")
    fun deleteFolder(folderId: Int,request: HttpServletRequest) : Any {
        if(!isValidId(folderId)) return INVALID_RESPONSE
        val uid = myUserInfo(request).id
        val responseBody = getFolderInfo(folderId,request)
        if(isSuccessResponse(responseBody)) {
            val data = jsonToMap(responseBody)["data"] as CollectionsFolderEntity
            return if(data.uid != uid) {
                // 删除的不是自己的收藏夹
                USER_FORBID_RESPONSE
            } else {
                // 删除自己的收藏夹
                val result = collectService.deleteFolder(folderId)
                if(result) {
                    ResultEntity.success("删除成功")
                } else {
                    DATABASE_ERROR_RESPONSE
                }
            }
        } else {
            // 解析失败
            return responseBody
        }
    }
//    @PutMapping("/folder/move")
//    fun moveToFolder(newFolderId : Int,collectionId : Int,request: HttpServletRequest) : Any {
//        if(!isValidId(newFolderId) && !isValidId(collectionId)) return INVALID_RESPONSE
//        // 只能操作
//    }
    @GetMapping("/info")
    fun getCollectionInfo(collectionId: Int) : Any {
        val result = collectService.getCollectionInfo(collectionId) ?: return EMPTY_RESPONSE
        return ResultEntity.success("查询成功",result)
    }
//    @GetMapping("/all/count")
//    fun getAllCollectionsCount(uid : Int) : Any {
//
//    }
    @GetMapping("/all/collection")
    fun getAllCollectionsList(request: HttpServletRequest) : Any {
        // 获取所有的收藏项目，仅对自己可用
        val session = myUserInfo(request)
        val result = collectService.getAllCollectionsList(session.id)
        return ResultEntity.success("查询成功",result)
    }
    @PostMapping("/collect")
    fun collect(
        articleId : Int,
        folderId : Int? = null,
        request: HttpServletRequest) : Any {
        // 参数验证
        if(!isValidId(articleId)) return INVALID_RESPONSE
        if(folderId != null && !isValidId(folderId)) return INVALID_RESPONSE
        // 数组组装
        val userinfo = myUserInfo(request)
        val result = collectService.collect(CollectionEntity(
            uid = userinfo.id,
            article_id = articleId,
            folder_id = folderId
        ))
        // 返回数据
        return if(result) {
            ResultEntity.success("收藏成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    @DeleteMapping("/uncollect")
    fun uncollect(collectionId : Int,request: HttpServletRequest) : Any {
        // 参数验证
        if(!isValidId(collectionId)) return INVALID_RESPONSE
        // 数组组装
        val userinfo = myUserInfo(request)
        val result = collectService.uncollect(collectionId,userinfo.id)
        // 返回数据
        return if(result) {
            ResultEntity.success("取消收藏成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    @PutMapping("/folder/change-state")
    fun changeFolderState(folderId: Int,state : String) : Any {
        val folderState = toState(state) ?: return INVALID_RESPONSE
        val result = collectService.changeFolderState(folderId,folderState)
        return if(result) {
            ResultEntity.success("修改成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    fun toState(state : String) : CollectionsFolderState? {
        return when(state) {
            CollectionsFolderState.PRIVATE.name -> CollectionsFolderState.PRIVATE
            CollectionsFolderState.PUBLIC.name -> CollectionsFolderState.PUBLIC
            else -> null
        }
    }

    // 获取某文章的收藏数量
    @GetMapping("/article/count")
    fun getArticleCollectionsCount(articleId : Int) : Any {
        if(!isValidId(articleId)) return INVALID_RESPONSE
        val count = collectService.getArticleCollectionsCount(articleId)
        return ResultEntity.success("查询成功", mapOf(
            "articleId" to articleId,
            "collectionsCount" to count
        ))
    }
}