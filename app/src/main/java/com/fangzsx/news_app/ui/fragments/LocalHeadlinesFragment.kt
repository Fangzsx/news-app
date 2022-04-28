package com.fangzsx.news_app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fangzsx.news_app.R
import com.fangzsx.news_app.adapters.NewsAdapter
import com.fangzsx.news_app.databinding.FragmentLocalHeadlinesBinding
import com.fangzsx.news_app.ui.NewsActivity
import com.fangzsx.news_app.util.Resource
import com.fangzsx.news_app.viewmodels.NewsViewModel


class LocalHeadlinesFragment : Fragment() {

    private lateinit var binding : FragmentLocalHeadlinesBinding
    private lateinit var newsAdapter: NewsAdapter

    lateinit var viewModel : NewsViewModel
    val TAG = "LocalHeadlinesFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocalHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        viewModel.getLocalHeadlines("ph")

        setupRecyclerView()
        
        viewModel.localHeadlines.observe(viewLifecycleOwner){ response ->
            when(response){
                is Resource.Success -> {
                    //hide progress bar
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured. $message")
                    }
                }

                is Resource.Loading -> {
                    Log.e(TAG, "show progress bar")
                }
            }

        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()

        binding.rvLocalHeadlines.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}