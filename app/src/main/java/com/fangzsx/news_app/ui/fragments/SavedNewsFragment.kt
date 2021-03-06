package com.fangzsx.news_app.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fangzsx.news_app.R
import com.fangzsx.news_app.adapters.NewsAdapter
import com.fangzsx.news_app.databinding.FragmentSavedNewsBinding
import com.fangzsx.news_app.ui.NewsActivity
import com.fangzsx.news_app.viewmodels.NewsViewModel
import com.google.android.material.snackbar.Snackbar

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

        newsAdapter.setOnReadMoreClickListener {
            val bundle = Bundle()
            bundle.putString("webview_url", it.url)
            bundle.putString("source", it.source!!.name)

            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
        }

        newsAdapter.setOnShareClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hi! I just wanna share this article with you. Click this link : ${it.url}")
            startActivity(Intent.createChooser(intent, "Share via"))
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)

                Snackbar.make(view, "Article deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }.show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }


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