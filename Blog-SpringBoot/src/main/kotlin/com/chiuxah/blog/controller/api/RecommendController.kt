package com.chiuxah.blog.controller.api

import com.chiuxah.blog.config.response.ResultEntity
import com.chiuxah.blog.controller.api.like.ArticleLikeController
import com.chiuxah.blog.model.bean.ArticleCount
import com.chiuxah.blog.model.bean.ArticleInfoSummary
import com.chiuxah.blog.model.bean.ArticleInfoWithCount
import com.chiuxah.blog.model.bean.UserInfoSummary
import com.chiuxah.blog.utils.ControllerUtils.isSuccessResponse
import com.chiuxah.blog.utils.ControllerUtils.jsonToMap
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 推荐模块
@RestController
@RequestMapping("/api/v1/recommendation")
class RecommendController {
    @Autowired
    lateinit var followController: FollowController
    @Autowired
    lateinit var articleController: ArticleController
    @Autowired
    lateinit var collectionController: CollectionController
    @Autowired
    lateinit var visitController: VisitController
    @Autowired
    lateinit var commentController: CommentController
    @Autowired
    lateinit var articleLikeController: ArticleLikeController
    // 推荐文章 显示在首页 从最近时间段拿几条，然后按热度（阅读量、点赞量、收藏量、评论量）综合排序， 每次获取得到的列表随机
    @GetMapping("/hot")
    fun getRecommendHotArticles(pageSize : Int = 15,page : Int = 1) : Any {
        // 初始化返回数据
        var hotArticles = mutableListOf<ArticleInfoWithCount>()
        // pageSize拿几条数据
        val articleInfos = articleController.articleService.getAllArticlesByPage(pageSize = pageSize*2, page = page)
        // articles已经时按时间顺序取得
        for(articleInfo in articleInfos) {
            val articleId = articleInfo.id
            // 初始化
            var likeCount = 0
            var visitCount = 0
            var collectCount = 0
            var commentCount = 0
            // 获取数据
            val likeCountResponse = articleLikeController.getArticleLikeCount(articleId)
            if(isSuccessResponse(likeCountResponse)) {
                likeCount = jsonToMap(likeCountResponse)["data"] as Int
            }
            val visitCountResponse = visitController.getArticleReadCount(articleId)
            if(isSuccessResponse(visitCountResponse)) {
                visitCount = jsonToMap(visitCountResponse)["data"] as Int
            }
            val collectCountResponse = collectionController.getArticleCollectionsCount(articleId)
            if(isSuccessResponse(collectCountResponse)) {
                collectCount = jsonToMap(collectCountResponse)["data"] as Int
            }
            val commentCountResponse = commentController.getCommentCount(articleId)
            if(isSuccessResponse(commentCountResponse)) {
                commentCount = jsonToMap(commentCountResponse)["data"] as Int
            }
            // 组装数据
            val countDTO = ArticleCount(
                likeCount = likeCount,
                visitCount = visitCount,
                collectCount = collectCount,
                commentCount = commentCount
            )
            val articleInfoWithCount = ArticleInfoWithCount(
                articleInfo = articleInfo,
                countInfo = countDTO
            )
            hotArticles.add(articleInfoWithCount)
        }
        // 从page*2中打乱随机挑选pageSize
        hotArticles = hotArticles.shuffled().take(pageSize).toMutableList()
        // 加权排序
        hotArticles.sortByDescending { item ->
            item.countInfo.visitCount * 1.0 +   // 阅读量权重 1.0
            item.countInfo.likeCount * 2.0 +    // 点赞权重 2.0
            item.countInfo.collectCount * 1.5 + // 收藏权重 2.0
            item.countInfo.commentCount * 3.0   // 评论权重 3.0
        }
        // 返回数据
        return ResultEntity.success("获取成功", data = hotArticles)
    }
    // 推荐文章 显示在首页  拿出自己关注的用户的文章，然后取最近时间段排序
    @GetMapping("/follow")
    fun getRecommendLikeArticles(request : HttpServletRequest) : Any {
        // 先取关注列表
        val followResponse = followController.getFolloweeList(request)
        if(!isSuccessResponse(followResponse)) {
            return followResponse
        }
        val followeeList = jsonToMap(followResponse)["data"] as List<UserInfoSummary>
        val totalArticles = mutableListOf<ArticleInfoSummary>()
        for(followee in followeeList) {
            // 关注者id
            val followeeId = followee.id
            // 查询他们的最近的文章
            val articlesResponse = articleController.getUserArticles(followeeId)
            if(!isSuccessResponse(articlesResponse)) {
                return articlesResponse
            }
            val articles = jsonToMap(articlesResponse) as List<ArticleInfoSummary>
            articles.forEach { item ->
                totalArticles.add(item)
            }
        }
        // 最后处理列表 按总的时间排序
        totalArticles.sortBy { item ->
            item.update_time
        }
        return ResultEntity.success("为您推荐关注${totalArticles.size}条", data = totalArticles)
    }
}