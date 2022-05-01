package com.fangzsx.news_app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fangzsx.news_app.R
import com.fangzsx.news_app.adapters.NewsAdapter
import com.fangzsx.news_app.databinding.FragmentInternationalHeadlinesBinding
import com.fangzsx.news_app.ui.NewsActivity
import com.fangzsx.news_app.util.Constants
import com.fangzsx.news_app.util.Resource
import com.fangzsx.news_app.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class InternationalHeadlinesFragment : Fragment() {

    private lateinit var binding : FragmentInternationalHeadlinesBinding
    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter : NewsAdapter
    private val TAG = "IntlHeadlinesFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInternationalHeadlinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        viewModel.getInternationalHeadlines("us")
        setupRecyclerView()

        newsAdapter.setOnReadMoreClickListener {
            val bundle = Bundle()
            bundle.putString("webview_url", it.url)
            bundle.putString("source", it.source!!.name)

            findNavController().navigate(R.id.action_internationalHeadlinesFragment_to_articleFragment,bundle)
        }

        newsAdapter.setOnSaveClickListener { article ->
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved.", Snackbar.LENGTH_SHORT).show()
        }

        newsAdapter.setOnShareClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hi! I just wanna share this article with you. Click this link : ${it.url}")
            startActivity(Intent.createChooser(intent, "Share via"))
        }

        viewModel.internationalHeadlines.observe(viewLifecycleOwner){ response ->
            when(response){
                is Resource.Success -> {
                    //hide progress bar
                    isLoading = false
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.internationalHeadlinesPageNumber == totalPages
                    }


                }

                is Resource.Error -> {
                    isLoading = false
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    isLoading = true
                    Log.e(TAG, "show progress bar")
                }
            }

        }


    }

    //scrolling
    var isScrolling = false
    var isLoading = false
    var isLastPage = false


    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }


        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisible + visibleItemCount >= totalItemCount
            val isAtNotBeginning = firstVisible >= 0
            val isMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isAtNotBeginning && isMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getInternationalHeadlines("us")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()

        binding.rvInternationalHeadlines.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@InternationalHeadlinesFragment.scrollListener)
        }
    }

}