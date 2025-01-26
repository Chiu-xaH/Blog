package com.chiuxah.blog.controller

import com.chiuxah.blog.config.AjaxResult
import com.chiuxah.blog.model.ArticleInfo
import com.chiuxah.blog.model.UserInfo
import com.chiuxah.blog.service.ArticleService
import com.chiuxah.blog.util.ConstVariable
import com.chiuxah.blog.util.state.ArticleState
import com.chiuxah.blog.util.state.ToState
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 博客模块
@RestController
@RequestMapping("/article")
class ArticleController {
    @Autowired lateinit var articleService: ArticleService
    // 发博客
    @RequestMapping("/add")
    fun add(request : HttpServletRequest,title : String,content : String) : Any {
        val session = request.session
        val ajaxResult = AjaxResult
        if(session?.getAttribute(ConstVariable.USER_SESSION_KEY) == null) {
            return ajaxResult.fail(-1,"未登录")
        }
        // 用户
        val userInfo = session.getAttribute(ConstVariable.USER_SESSION_KEY) as UserInfo
        val uid = userInfo.id
        val rcount = 1 // 初始化阅读量为1
        val state = ToState.getArticleState(ArticleState.PUBLISHED) // 初始化状态为发布
        val articleInfo = ArticleInfo(
            title = title,
            content = content,
            uid = uid,
            rcount = rcount,
            state = state,
        )
        val result = articleService.add(articleInfo)
        return if(result == 1) {
            ajaxResult.success("发布成功",1)
        } else {
            ajaxResult.fail(-1,"发布失败")
        }
    }
    // 查询指定用户的博客
//    @RequestMapping("/get_mylist")
//    fun
}