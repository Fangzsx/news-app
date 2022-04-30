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

    val localHeadlines : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var localHeadlinesPageNumber = 1
    var localHeadlinesResponse : NewsResponse? = null

    val internationalHeadlines : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var internationalHeadlinesPageNumber = 1
    var internationalHeadlinesResponse : NewsResponse? = null



    val searchNewsResult : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsResultPageNumber = 1
    var searchNewsResponse : NewsResponse? = null


    fun getLocalHeadlines(countryCode : String) = viewModelScope.launch {
        localHeadlines.postValue(Resource.Loading())
        val response = repository.getHeadlines(countryCode, localHeadlinesPageNumber)
        localHeadlines.postValue(handleLocalNewsResponse(response))
    }

    fun getInternationalHeadlines(countryCode : String) = viewModelScope.launch {
        internationalHeadlines.postValue(Resource.Loading())
        val response = repository.getHeadlines(countryCode, internationalHeadlinesPageNumber)
        internationalHeadlines.postValue(handleInternationalNewsResponse(response))
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
        searchNewsResult.postValue(Resource.Loading())
        val response = repository.searchNews(searchQuery, searchNewsResultPageNumber)
        searchNewsResult.postValue(handleSearchNewsResponse(response))

    }

    private fun handleLocalNewsResponse(response : Response<NewsResponse>) :  Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                localHeadlinesPageNumber++
                if(localHeadlinesResponse == null){
                    localHeadlinesResponse = resultResponse
                }else{
                    val oldArticles = localHeadlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(localHeadlinesResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleInternationalNewsResponse(response : Response<NewsResponse>) :  Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                internationalHeadlinesPageNumber++
                if(internationalHeadlinesResponse == null){
                    internationalHeadlinesResponse = resultResponse
                }else{
                    val oldArticles = internationalHeadlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }

                return Resource.Success(internationalHeadlinesResponse ?: resultResponse)
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