package com.chiuxah.blog.controller

import com.chiuxah.blog.config.AjaxResult
import com.chiuxah.blog.model.ImageInfo
import com.chiuxah.blog.model.UserInfo
import com.chiuxah.blog.service.ImageService
import com.chiuxah.blog.util.ConstVariable
import com.chiuxah.blog.util.state.ImageType
import com.chiuxah.blog.util.state.StatusCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.lang.Exception
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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

    // 查看用户上传的图片
    @GetMapping("get_uploaded")
    fun getUploadedImage(request: HttpServletRequest) : Any {
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo
        val uid = session.id
        val imgList = imageService.selectByUid(uid)
        return AjaxResult.success(data = imgList)
    }
    // 删除图片 数据库删除，并移除实体文件
    @DeleteMapping("del")
    fun delImage() {

    }
    // 更换头像 上传图片后，保存用户数据库的photo为旧的URL，然后设置photo为新的url 然后根据旧URL检索删掉旧的图片实体文件及其数据库记录
    @PostMapping("update_user_photo")
    fun uploadUserPhoto(
        image : MultipartFile,
        request : HttpServletRequest
    )  : Any {
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
            } else {
                AjaxResult.success("更新成功")
            }
        } else {
            AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"上传失败")
        }
    }
}