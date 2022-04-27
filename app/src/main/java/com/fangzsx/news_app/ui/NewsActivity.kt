package com.fangzsx.news_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fangzsx.news_app.R
import com.fangzsx.news_app.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.findNavController()

        binding.bottomNav.setupWithNavController(navController)

    }
}