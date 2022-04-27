package com.fangzsx.news_app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fangzsx.news_app.repo.NewsRepository

class NewsViewModelFactory(
    val repository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }


}