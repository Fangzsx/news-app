package com.fangzsx.news_app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fangzsx.news_app.R
import com.fangzsx.news_app.adapters.SearchResultAdapter
import com.fangzsx.news_app.databinding.FragmentSearchNewsBinding
import com.fangzsx.news_app.ui.NewsActivity
import com.fangzsx.news_app.util.Resource
import com.fangzsx.news_app.viewmodels.NewsViewModel


class SearchNewsFragment : Fragment() {

    private lateinit var binding : FragmentSearchNewsBinding
    lateinit var viewModel : NewsViewModel
    lateinit var searchResultAdapter: SearchResultAdapter

    private val TAG = "SearchNewsFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let{ submitQuery ->
                    if(submitQuery.isNotEmpty()){
                        viewModel.searchNews(submitQuery)
                    }
                }
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })



        viewModel.searchNewsResult.observe(viewLifecycleOwner){ response ->
            when(response){
                is Resource.Success -> {
                    //hide progressbar
                    response.data?.let { newsResponse ->
                        searchResultAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    response.message?.let{ message ->
                        Log.e(TAG, "An error occurred. $message")
                    }
                }

                is Resource.Loading -> {
                    //show progress bar
                }
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        searchResultAdapter = SearchResultAdapter()

        binding.rvSearchResults.apply{
            adapter = searchResultAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}