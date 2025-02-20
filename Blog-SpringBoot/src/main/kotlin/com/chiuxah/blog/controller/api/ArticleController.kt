package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.model.entity.ArticleEntity
import com.chiuxah.blog.model.enums.state.ArticleState
import com.chiuxah.blog.service.ArticleService
import com.chiuxah.blog.utils.ControllerUtils.DATABASE_ERROR_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.EMPTY_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.INVALID_RESPONSE
import com.chiuxah.blog.utils.ControllerUtils.USER_FORBID_RESPONSE
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
    fun addArticle(request : HttpServletRequest, title : String, content : String) : Any {
        // 用户
        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        val state = ArticleState.PUBLISHED.state // 初始化状态为发布 审核功能后期再加
        val articleInfo = ArticleEntity(
            title = title,
            content = content,
            uid = uid,
            state = state,
        )
        val result = articleService.add(articleInfo)
        return if(result) {
            ResultEntity.success("发布成功")
        } else {
            DATABASE_ERROR_RESPONSE
        }
    }
    // 管理我的博客
    @GetMapping("/mine")
    fun getMyBlogList(request: HttpServletRequest) : Any {
        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        return ResultEntity.success("查找成功",articleService.getUserArticles(uid))
    }
    // 查询指定用户的博客
    @GetMapping("/user")
    fun getUserArticles(uid : Int) : Any {
        if(!isValidId(uid)) {
            return INVALID_RESPONSE
        }
        return ResultEntity.success("查找成功",articleService.getUserArticles(uid))
    }
    // 博客详情
    @GetMapping("/info")
    fun getArticleInfo(articleId : Int) : Any {
        if(!isValidId(articleId)) {
            return INVALID_RESPONSE
        }
        val articleInfo = articleService.getArticleInfo(articleId) ?: EMPTY_RESPONSE
        return ResultEntity.success(data = articleInfo)
    }
    // 获取所有博客
    @GetMapping("/all")
    fun getAllArticles() : Any {
        return ResultEntity.success(data = articleService.getAllArticles())
    }
    // 删除博客
    @DeleteMapping("/del")
    fun delArticle(articleId : Int, request : HttpServletRequest, response: HttpServletResponse) : Any {
        if(!isValidId(articleId)) {
            return INVALID_RESPONSE
        }
        val articleInfo = articleService.getArticleInfo(articleId)
            ?: return EMPTY_RESPONSE

        val userInfo = myUserInfo(request)
        return if(userInfo.id != articleInfo.uid) {
            USER_FORBID_RESPONSE
        } else {
            val result = articleService.del(articleId)
            if(!result) {
                DATABASE_ERROR_RESPONSE
            } else {
                ResultEntity.success(msg = "删除成功")
            }
        }
    }
    // 更新博客内容/标题
    @PutMapping("/update")
    fun updateArticle(request: HttpServletRequest, articleId : Int, title: String?, content: String?) : Any {
        if(!isValidId(articleId)) {
            return INVALID_RESPONSE
        }
        val articleInfo = articleService.getArticleInfo(articleId) ?: return EMPTY_RESPONSE

        val userInfo = myUserInfo(request)
        val uid = userInfo.id
        // 验证是否为博文的作者，只有作者有权限操作自己的博文
        return if(uid != articleInfo.uid) {
            USER_FORBID_RESPONSE
        } else {
            val result = articleService.update(articleId,title,content)
            if(!result) {
                DATABASE_ERROR_RESPONSE
            } else {
                ResultEntity.success("更新成功")
            }
        }
    }
}
