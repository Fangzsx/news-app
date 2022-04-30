package com.fangzsx.news_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fangzsx.news_app.R
import com.fangzsx.news_app.adapters.NewsAdapter
import com.fangzsx.news_app.databinding.FragmentSavedNewsBinding
import com.fangzsx.news_app.ui.NewsActivity
import com.fangzsx.news_app.viewmodels.NewsViewModel

class SavedNewsFragment : Fragment() {

    private lateinit var binding : FragmentSavedNewsBinding
    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        setupRecyclerView()

        viewModel.getSavedNews().observe(viewLifecycleOwner){ articles ->
            newsAdapter.differ.submitList(articles)
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        newsAdapter.isSaveVisible = false

        binding.rvSavedNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}