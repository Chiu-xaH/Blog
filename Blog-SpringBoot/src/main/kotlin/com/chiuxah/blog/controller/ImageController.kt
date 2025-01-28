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

        val filename = UUID.randomUUID().toString()
        val filePath = File(uploadDir,filename)
        // 保存文件
        try {
            image.transferTo(filePath)

            val url = "/$uploadDir/$filename"

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
    fun getUploadedImage() {

    }
    // 删除图片 数据库删除，并移除实体文件
    @DeleteMapping("del")
    fun delImage() {

    }
    // 更换头像 上传图片后，保存用户数据库的photo为旧的URL，然后设置photo为新的url 然后根据旧URL检索删掉旧的图片实体文件及其数据库记录
    @PostMapping("upload_user_photo")
    fun uploadUserPhoto() {

    }
}