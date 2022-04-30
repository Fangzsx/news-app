package com.fangzsx.news_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fangzsx.news_app.model.Article
import com.fangzsx.news_app.model.NewsResponse
import com.fangzsx.news_app.repo.NewsRepository
import com.fangzsx.news_app.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    val headlines : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPageNumber = 1
    var headlinesResponse : NewsResponse? = null

    val searchNewsResult : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsResultPageNumber = 1
    var searchNewsResponse : NewsResponse? = null


    fun getHeadlines(countryCode : String) = viewModelScope.launch {
        headlines.postValue(Resource.Loading())
        val response = repository.getLocalHeadlines(countryCode, headlinesPageNumber)
        headlines.postValue(handleLocalNewsResponse(response))
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
        searchNewsResult.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery, searchNewsResultPageNumber)
        searchNewsResult.postValue(handleSearchNewsResponse(response))

    }

    private fun handleLocalNewsResponse(response : Response<NewsResponse>) :  Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                headlinesPageNumber++
                if(headlinesResponse == null){
                    headlinesResponse = resultResponse
                }else{
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response : Response<NewsResponse>) :  Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                searchNewsResultPageNumber++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun getSavedNews() = repository.getAllArticle()

    fun saveArticle(article : Article) = viewModelScope.launch {
        repository.upsertArticle(article)
    }

    fun deleteArticle(article : Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

}