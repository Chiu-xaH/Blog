package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.model.bean.ImageBean
import com.chiuxah.blog.service.ImageService
import com.chiuxah.blog.model.enums.ImageType
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.model.bean.UserSessionSummary
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.jsonToMap
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.lang.Exception
import java.util.*
// 图片上传模块
@RestController
@RequestMapping("/api/v1/image")
class ImageController {
    @Autowired
    lateinit var imageService : ImageService

    @Value("\${file.upload-dir}") // 保存路径
    lateinit var uploadDir: String

    @Value("\${file.location-url}") // 映射路径
    lateinit var locationUrl: String
    // 上传图片返回URL、id
    @PostMapping("/upload")
    fun uploadImage(
        image : MultipartFile,
        type : String,
        request : HttpServletRequest
    ) : Any {
        val userinfo = myUserInfo(request)

        val types = when(type) {
            ImageType.BLOG_PHOTO.str -> ImageType.BLOG_PHOTO
            ImageType.USER_PHOTO.str -> ImageType.USER_PHOTO
            else -> return INVALID_RESPONSE
        }

        val originalFilename = image.originalFilename ?: ""
        val extension = originalFilename.substringAfterLast(".", "").lowercase()

        if (extension.isEmpty()) {
            return ResultEntity.fail(StatusCode.BAD_REQUEST, "无法识别文件格式")
        }
        val filename = "${UUID.randomUUID()}.$extension" // 生成带扩展名的唯一文件名
        val filePath = File(uploadDir, filename)

        // 保存文件
        try {
            image.transferTo(filePath)

            val url = "$locationUrl$filename"

            val fileSize = filePath.length()
            val fileType = image.contentType ?: "unknown"

            val imageInfo = ImageBean(
                url = url,
                size = fileSize,
                filename = filename,
                filetype = fileType,
                type = types.code,
                uid = userinfo.id,
            )
            val result = imageService.addImage(imageInfo)
            return if(result <= 0) {
                ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"插入数据库失败")
            } else {
                ResultEntity.success("上传成功", data = mapOf(
                    "id" to imageInfo.id,
                    "url" to url
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"上传失败")
        }
    }

    // 根据文件名查看图片信息
    @GetMapping("/info")
    fun getImage(filename : String) : Any {
        val result = imageService.selectByFilename(filename)
            ?: return ResultEntity.success("未查询到")
        return ResultEntity.success("查询成功",result)
    }
    // 查看用户上传的图片
    @GetMapping("/mine")
    fun getUploadedImage(request: HttpServletRequest) : Any {
        val session = myUserInfo(request)
        val uid = session.id
        val imgList = imageService.selectByUid(uid)
        return ResultEntity.success(data = imgList)
    }
    // 删除图片 : 根据文件名删除数据库,然后移除实体文件
    @DeleteMapping("/del")
    fun delImage(filename : String,request: HttpServletRequest) : Any {
        // 查找图片
        val responseBody = getImage(filename)
        if(responseBody is Map<*,*> && responseBody["data"] != null) {
            val imageinfo = responseBody["data"] as? ImageBean ?: return ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"解析上传结果失败")
            val uid = imageinfo.uid
            // 权限检查
            val session = myUserInfo(request)
            val user = session.id
            if(user != uid) {
                return ResultEntity.fail(StatusCode.FORBIDDEN,"权限不足")
            }
            // 移除数据库记录
            val result = imageService.delByFilename(filename)
            if(!result) {
                return ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"删除记录失败")
            }
            // 移除实体文件
            return delFile(filename)
        } else {
            return ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"查找图片失败")
        }
    }
    // 更换头像 上传图片后，保存用户数据库的photo为旧的URL，然后设置photo为新的url 然后根据旧URL检索删掉旧的图片实体文件及其数据库记录
    @PutMapping("/update-user-photo")
    fun uploadUserPhoto(
        image : MultipartFile,
        request : HttpServletRequest
    )  : Any {
        // 保存旧头像URL,提取文件名
        val userinfo = myUserInfo(request)
        val oldFilename = userinfo.photo.substringAfter(locationUrl)

        val responseBody = uploadImage(image, ImageType.USER_PHOTO.str,request)
        return if(isSuccessResponse(responseBody)) {
            // 将响应URL设置为用户信息的photo
            val data = jsonToMap(responseBody)["data"] ?: return ResultEntity.fail(
                StatusCode.INTERNAL_SERVER_ERROR,
                "解析失败"
            )
            val url = jsonToMap(data)["url"] as String

            val uid = userinfo.id
            val result = imageService.updateUserPhoto(uid, url)
            if (!result) {
                ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR, "更新失败")
            }
            // 删除旧头像
            if (oldFilename.isNotEmpty()) {
                delImage(oldFilename, request)
            }
            // 更新cookie
            request.session.setAttribute(ConstVariable.USER_SESSION_KEY, UserSessionSummary(
                id = userinfo.id,
                username = userinfo.username,
                password = userinfo.password,
                photo = url,
                email = userinfo.email
            ))
            return ResultEntity.success("更新成功")
        } else {
            ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"上传失败")
        }
    }
    // 清理无家可归的实体文件 如果实体文件存在，但是数据库查询不到，则删除
    @DeleteMapping("/recycle")
    fun recycle() {
        // 预置方法 传入filename 查找数据库返回ImageInfo?,查询不到为null
        val uploadDirFile = File(uploadDir)
        if (uploadDirFile.exists() && uploadDirFile.isDirectory) {
            val files = uploadDirFile.listFiles()
            files?.forEach { file ->
                if (file.isFile) {
                    val result = imageService.selectByFilename(file.name)
                    if (result == null) {
                        val delResult = delFile(file.name)
                        println("已删除无关联文件：${file.name}，结果：$delResult")
                    }
                }
            }
        }
    }
    // 移除实体文件
    fun delFile(filename: String) : Any {
        val file = File(uploadDir, filename)
        if (file.exists()) {
            val deleted = file.delete()
            if (!deleted) {
                return ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR, "文件删除失败")
            }
        } else {
            return ResultEntity.fail(StatusCode.NOT_FOUND, "文件不存在")
        }
        return ResultEntity.success("删除成功")
    }
}