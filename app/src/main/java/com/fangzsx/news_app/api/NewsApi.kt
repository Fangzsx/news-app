package com.fangzsx.news_app.api

import com.fangzsx.news_app.model.NewsResponse
import com.fangzsx.news_app.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getLocalHeadlines(
        @Query("country")
        countryCode : String = "ph",
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        query : String,
        @Query("page")
        pageNumber : Int = 1,
        @Query("apiKey")
        apiKey : String = API_KEY
    ) : Response<NewsResponse>

}