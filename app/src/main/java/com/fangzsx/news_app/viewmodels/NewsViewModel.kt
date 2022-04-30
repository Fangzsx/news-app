package com.fangzsx.news_app.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fangzsx.news_app.NewsApp
import com.fangzsx.news_app.model.Article
import com.fangzsx.news_app.model.NewsResponse
import com.fangzsx.news_app.repo.NewsRepository
import com.fangzsx.news_app.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app : Application,
    private val repository: NewsRepository
) : AndroidViewModel(app) {

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
        safeLocalHeadlines(countryCode)
    }

    fun getInternationalHeadlines(countryCode : String) = viewModelScope.launch {
        internationalHeadlines.postValue(Resource.Loading())
        safeInternationalHeadlines(countryCode)
    }

    fun searchNews(searchQuery : String) = viewModelScope.launch {
        searchNewsResult.postValue(Resource.Loading())
        safeSearchNews(searchQuery)

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

    private suspend fun safeLocalHeadlines(countryCode : String){
        localHeadlines.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = repository.getHeadlines(countryCode, localHeadlinesPageNumber)
                localHeadlines.postValue(handleLocalNewsResponse(response))
            }
        }catch (t : Throwable){
            when (t){
                is IOException -> localHeadlines.postValue(Resource.Error("Network Failure"))
                else -> localHeadlines.postValue(Resource.Error("JSON Conversion error occurred."))
            }
        }
    }

    private suspend fun safeInternationalHeadlines(countryCode : String){
        localHeadlines.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = repository.getHeadlines(countryCode, internationalHeadlinesPageNumber)
                internationalHeadlines.postValue(handleInternationalNewsResponse(response))
            }
        }catch (t : Throwable){
            when (t){
                is IOException -> internationalHeadlines.postValue(Resource.Error("Network Failure"))
                else -> internationalHeadlines.postValue(Resource.Error("JSON Conversion error occurred."))
            }
        }
    }

    private suspend fun safeSearchNews(query : String){
        searchNewsResult.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = repository.searchNews(query, searchNewsResultPageNumber)
                searchNewsResult.postValue(handleSearchNewsResponse(response))
            }
        }catch (t : Throwable){
            when (t){
                is IOException -> searchNewsResult.postValue(Resource.Error("Network Failure"))
                else -> searchNewsResult.postValue(Resource.Error("JSON Conversion error occurred."))
            }
        }
    }

    private fun hasInternetConnection() : Boolean{
        val connectivityManager = getApplication<NewsApp>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else{
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI ->true
                    TYPE_MOBILE ->true
                    TYPE_ETHERNET ->true
                    else -> false
                }
            }
        }
        return false
    }

}