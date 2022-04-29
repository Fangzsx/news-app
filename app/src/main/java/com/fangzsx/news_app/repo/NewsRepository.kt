package com.fangzsx.news_app.repo

import com.fangzsx.news_app.api.RetrofitInstance
import com.fangzsx.news_app.db.ArticleDatabase
import com.fangzsx.news_app.model.Article

class NewsRepository(
    val db : ArticleDatabase
) {

    suspend fun getLocalHeadlines(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getLocalHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun upsertArticle(article : Article) = db.getArticleDao().upsertArticle(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getAllArticle() = db.getArticleDao().getAllArticles()
}