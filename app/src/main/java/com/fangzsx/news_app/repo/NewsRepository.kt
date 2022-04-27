package com.fangzsx.news_app.repo

import com.fangzsx.news_app.api.RetrofitInstance
import com.fangzsx.news_app.db.ArticleDatabase

class NewsRepository(
    val db : ArticleDatabase
) {

    suspend fun getLocalHeadlines(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getLocalHeadlines(countryCode, pageNumber)

    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)
}