package com.fangzsx.news_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fangzsx.news_app.R
import com.fangzsx.news_app.databinding.ActivityNewsBinding
import com.fangzsx.news_app.db.ArticleDatabase
import com.fangzsx.news_app.repo.NewsRepository
import com.fangzsx.news_app.viewmodels.NewsViewModel
import com.fangzsx.news_app.viewmodels.NewsViewModelFactory

class NewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewsBinding
    lateinit var viewModel : NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository : NewsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.findNavController()

        binding.bottomNav.setupWithNavController(navController)

    }
}