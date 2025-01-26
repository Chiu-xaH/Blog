package com.chiuxah.blog.util.state

object ToState {
    fun getArticleState(articleState: ArticleState) : Int {
        return when(articleState) {
            ArticleState.PUBLISHED -> 1
            ArticleState.EDITING -> 2
        }
    }
}