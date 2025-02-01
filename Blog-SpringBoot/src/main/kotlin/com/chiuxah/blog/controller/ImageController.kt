package com.chiuxah.blog.controller

import com.chiuxah.blog.config.AjaxResult
import com.chiuxah.blog.model.ImageInfo
import com.chiuxah.blog.model.UserInfo
import com.chiuxah.blog.service.ImageService
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.utils.enums.ImageType
import com.chiuxah.blog.utils.enums.StatusCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.lang.Exception
import java.util.*

// 图片上传模块
@RestController
@RequestMapping("/image")
class ImageController {
    @Autowired lateinit var imageService : ImageService

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
        val userinfo = request.session.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo

        val types = when(type) {
            ImageType.BLOG_PHOTO.str -> ImageType.BLOG_PHOTO
            ImageType.USER_PHOTO.str -> ImageType.USER_PHOTO
            else -> return AjaxResult.fail(StatusCode.BAD_REQUEST,"参数有误")
        }

        val originalFilename = image.originalFilename ?: ""
        val extension = originalFilename.substringAfterLast(".", "").lowercase()

        if (extension.isEmpty()) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST, "无法识别文件格式")
        }
        val filename = "${UUID.randomUUID()}.$extension" // 生成带扩展名的唯一文件名
        val filePath = File(uploadDir, filename)

        // 保存文件
        try {
            image.transferTo(filePath)

            val url = "$locationUrl$filename"

            val fileSize = filePath.length()
            val fileType = image.contentType ?: "unknown"

            val imageInfo = ImageInfo(
                url = url,
                size = fileSize,
                filename = filename,
                state = 1,
                filetype = fileType,
                type = types.code,
                uid = userinfo.id,
            )
            val result = imageService.addImage(imageInfo)
            return if(result <= 0) {
                AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"插入数据库失败")
            } else {
                AjaxResult.success("上传成功", data = mapOf(
                    "id" to imageInfo.id,
                    "url" to url
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"上传失败")
        }
    }

    // 根据文件名查看图片信息
    @GetMapping("/get")
    fun getImage(filename : String) : Any {
        val result = imageService.selectByFilename(filename)
            ?: return AjaxResult.success("未查询到")
        return AjaxResult.success("查询成功",result)
    }
    // 查看用户上传的图片
    @GetMapping("get_uploaded")
    fun getUploadedImage(request: HttpServletRequest) : Any {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo
        val uid = session.id
        val imgList = imageService.selectByUid(uid)
        return AjaxResult.success(data = imgList)
    }
    // 删除图片 : 根据文件名删除数据库,然后移除实体文件
    @DeleteMapping("del")
    fun delImage(filename : String,request: HttpServletRequest) : Any {
        // 查找图片
        val responseBody = getImage(filename)
        if(responseBody is Map<*,*> && responseBody["data"] != null) {
            val imageinfo = responseBody["data"] as? ImageInfo ?: return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"解析上传结果失败")
            val uid = imageinfo.uid
            // 权限检查
            val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo
            val user = session.id
            if(user != uid) {
                return AjaxResult.fail(StatusCode.FORBIDDEN,"权限不足")
            }
            // 移除数据库记录
            val result = imageService.delByFilename(filename)
            if(result <= 0) {
                return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"删除记录失败")
            }
            // 移除实体文件
            return delFile(filename)
        } else {
            return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"查找图片失败")
        }
    }
    // 更换头像 上传图片后，保存用户数据库的photo为旧的URL，然后设置photo为新的url 然后根据旧URL检索删掉旧的图片实体文件及其数据库记录
    @PostMapping("update_user_photo")
    fun uploadUserPhoto(
        image : MultipartFile,
        request : HttpServletRequest
    )  : Any {
        // 保存旧头像URL,提取文件名
        val userinfo = (request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)) as UserInfo
        val oldFilename = userinfo.photo.substringAfter(locationUrl)

        val responseBody = uploadImage(image,ImageType.USER_PHOTO.str,request)
        return if(responseBody is Map<*, *> && responseBody["state"] == StatusCode.SUCCESS.code) {
            // 将响应URL设置为用户信息的photo
            val data = responseBody["data"] as? Map<*, *> ?: return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR, "解析上传结果失败")
            val url = data["url"] as? String ?: return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR, "未获取到图片URL")

            val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo
            val uid = session.id
            val result = imageService.updateUserPhoto(uid,url)
            if(result <= 0) {
                AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"更新失败")
            }
            // 删除旧头像
            if(oldFilename.isNotEmpty()) {
               delImage(oldFilename,request)
            }
            return  AjaxResult.success("更新成功")
        } else {
            AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"上传失败")
        }
    }
    // 清理无家可归的实体文件 如果实体文件存在，但是数据库查询不到，则删除
    @DeleteMapping("recycle")
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
                return AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR, "文件删除失败")
            }
        } else {
            return AjaxResult.fail(StatusCode.NOT_FOUND, "文件不存在")
        }
        return AjaxResult.success("删除成功")
    }
}