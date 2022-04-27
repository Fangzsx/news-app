package com.fangzsx.news_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fangzsx.news_app.model.NewsResponse
import com.fangzsx.news_app.repo.NewsRepository
import com.fangzsx.news_app.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    val localHeadlines : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPageNumber = 1


    init {
        getLocalHeadlines("ph")
    }

    fun getLocalHeadlines(countryCode : String) = viewModelScope.launch {
        localHeadlines.postValue(Resource.Loading())
        val response = repository.getLocalHeadlines(countryCode, headlinesPageNumber)

        localHeadlines.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response : Response<NewsResponse>) :  Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

}