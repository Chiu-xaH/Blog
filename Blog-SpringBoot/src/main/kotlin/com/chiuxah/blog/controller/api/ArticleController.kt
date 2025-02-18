package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.config.response.StatusCode
import com.chiuxah.blog.model.bean.ArticleBean
import com.chiuxah.blog.model.enums.ArticleState
import com.chiuxah.blog.service.ArticleService
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.myUserInfo
import com.chiuxah.blog.utils.ValidUtils.isValidId
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

// 博客模块
@RestController
@RequestMapping("/api/v1/article")
class ArticleController {
    @Autowired
    lateinit var articleService: ArticleService
    // 发博客
    @PostMapping("/add")
    fun addBlog(request : HttpServletRequest,title : String,content : String) : Any {
        // 用户
        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        val state = ArticleState.CHECKING.state // 初始化状态为发布 审核功能后期再加
        val articleInfo = ArticleBean(
            title = title,
            content = content,
            uid = uid,
            state = state,
        )
        val result = articleService.add(articleInfo)
        return if(result) {
            ResultEntity.success("发布成功")
        } else {
            ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"发布失败")
        }
    }
    // 管理我的博客
    @GetMapping("/mine")
    fun getMyBlogList(request: HttpServletRequest) : Any {
        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        return ResultEntity.success("查找成功",articleService.getBlogListByUser(uid))
    }
    // 查询指定用户的博客
    @GetMapping("/user")
    fun getUserBlogList(uid : Int) : Any {
        if(!isValidId(uid)) {
            return INVALID_RESPONSE
        }
        return ResultEntity.success("查找成功",articleService.getBlogListByUser(uid))
    }
    // 博客详情
    @GetMapping("/info")
    fun getByBlogId(id : Int) : Any {
        if(!isValidId(id)) {
            return INVALID_RESPONSE
        }
        val articleInfo = articleService.selectByBlogId(id)
        return ResultEntity.success(data = articleInfo)
    }
    // 获取所有博客
    @GetMapping("/all")
    fun getBlogList() : Any {
        return ResultEntity.success(data = articleService.getBlogList())
    }
    // 删除博客
    @DeleteMapping("/del")
    fun delBlog(id : Int,request : HttpServletRequest,response: HttpServletResponse) : Any {
        if(!isValidId(id)) {
            return INVALID_RESPONSE
        }
        val articleInfo = articleService.selectByBlogId(id)
            ?: return ResultEntity.fail(StatusCode.NOT_FOUND,"无此博文")

        val userInfo = myUserInfo(request)
        return if(userInfo.id != articleInfo.uid) {
            ResultEntity.fail(StatusCode.FORBIDDEN,"无权限")
        } else {
            val result = articleService.del(id)
            if(!result) {
                ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"删除失败")
            } else {
                ResultEntity.success(msg = "删除成功")
            }
        }
    }
    // 更新博客内容/标题
    @PutMapping("/update")
    fun updateBlog(request: HttpServletRequest,id : Int,title: String?,content: String?) : Any {
        if(!isValidId(id)) {
            return INVALID_RESPONSE
        }
        val articleInfo = articleService.selectByBlogId(id) ?: return ResultEntity.fail(StatusCode.NOT_FOUND,"找不到博文")

        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        // 验证是否为博文的作者，只有作者有权限操作自己的博文
        return if(uid != articleInfo.uid) {
            ResultEntity.fail(StatusCode.FORBIDDEN,"无权限")
        } else {
            val result = articleService.update(id,title,content)
            if(!result) {
                ResultEntity.fail(StatusCode.INTERNAL_SERVER_ERROR,"更新失败")
            } else {
                ResultEntity.success("更新成功")
            }
        }
    }
}
