package com.chiuxah.blog.controller

import com.chiuxah.blog.config.AjaxResult
import com.chiuxah.blog.model.ArticleInfo
import com.chiuxah.blog.model.UserInfo
import com.chiuxah.blog.service.ArticleService
import com.chiuxah.blog.utils.ConstVariable
import com.chiuxah.blog.utils.enums.ArticleState
import com.chiuxah.blog.utils.enums.StatusCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 博客模块
@RestController
@RequestMapping("/article")
class ArticleController {
    @Autowired lateinit var articleService: ArticleService
    // 发博客
    @RequestMapping("/add")
    fun addBlog(request : HttpServletRequest,title : String,content : String) : Any {
        val session = request.session
//        if(session?.getAttribute(ConstVariable.USER_SESSION_KEY) == null) {
//            return AjaxResult.fail(StatusCode.UNAUTHORIZED,"未登录")
//        }
        // 用户
        val userInfo = session.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo
        val uid = userInfo.id
        val rcount = 1 // 初始化阅读量为1
        val state = ArticleState.PUBLISHED.state // 初始化状态为发布
        val articleInfo = ArticleInfo(
            title = title,
            content = content,
            uid = uid,
            rcount = rcount,
            state = state,
        )
        val result = articleService.add(articleInfo)
        return if(result == 1) {
            AjaxResult.success("发布成功")
        } else {
            AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"发布失败")
        }
    }
    // 管理我的博客
    @GetMapping("/get_my_list")
    fun getMyBlogList(request: HttpServletRequest) : Any {
        val session = request.session.getAttribute(ConstVariable.USER_SESSION_KEY)
//            ?: return AjaxResult.fail(StatusCode.UNAUTHORIZED,"未登录")
        val userInfo = session as UserInfo
        val uid = userInfo.id
        return AjaxResult.success("查找成功",articleService.getBlogListByUser(uid))
    }
    // 查询指定用户的博客
    @GetMapping("/get_user_list")
    fun getUserBlogList(uid : Int) : Any {
        if(uid <= 0) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST,"传入参数有误")
        }
        return AjaxResult.success("查找成功",articleService.getBlogListByUser(uid))
    }
    // 博客详情
    @GetMapping("/get_by_blog_id")
    fun getByBlogId(id : Int) : Any {
        if(id <= 0) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST,"传入参数有误")
        }
        val articleInfo = articleService.selectByBlogId(id)
        return if(articleInfo == null) {
            AjaxResult.success(data = emptyList<ArticleInfo>())
        } else {
            AjaxResult.success(data = articleInfo)
        }
    }
    // 获取所有博客
    @GetMapping("/get_list")
    fun getBlogList() : Any {
        return AjaxResult.success(data = articleService.getBlogList())
    }
    // 删除博客
    @DeleteMapping("/del")
    fun delBlog(id : Int,request : HttpServletRequest,response: HttpServletResponse) : Any {
        if(id <= 0) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST,"参数有误")
        }
        val articleInfo = articleService.selectByBlogId(id)
            ?: return AjaxResult.fail(StatusCode.BAD_REQUEST,"无此博文")

        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
//            ?: return AjaxResult.fail(StatusCode.UNAUTHORIZED,"未登录")

        val userInfo = session as UserInfo
        return if(userInfo.id != articleInfo.uid) {
            AjaxResult.fail(StatusCode.FORBIDDEN,"无权限")
        } else {
            val result = articleService.del(id)
            if(result <= 0) {
                AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"删除失败")
            } else {
                AjaxResult.success(msg = "删除成功")
            }
        }
    }
    // 更新博客内容/标题
    @RequestMapping("/update")
    fun updateBlog(request: HttpServletRequest,id : Int,title: String?,content: String?) : Any {
        if(id <= 0) {
            return AjaxResult.fail(StatusCode.BAD_REQUEST,"参数有误")
        }
        val articleInfo = articleService.selectByBlogId(id) ?: return AjaxResult.fail(StatusCode.BAD_REQUEST,"找不到博文")
        // 验证是否登录
        val session = request.session?.getAttribute(ConstVariable.USER_SESSION_KEY)
//            ?: return AjaxResult.fail(StatusCode.UNAUTHORIZED,"未登录")
        val userInfo = session as UserInfo
        val uid = userInfo.id
        // 验证是否为博文的作者，只有作者有权限操作自己的博文
        return if(uid != articleInfo.uid) {
            AjaxResult.fail(StatusCode.FORBIDDEN,"无权限")
        } else {
            val result = articleService.update(id,title,content)
            if(result <= 0) {
                AjaxResult.fail(StatusCode.INTERNAL_SERVER_ERROR,"更新失败")
            } else {
                AjaxResult.success("更新成功")
            }
        }
    }
}
