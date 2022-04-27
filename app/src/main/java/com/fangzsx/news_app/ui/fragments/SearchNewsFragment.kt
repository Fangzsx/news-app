package com.fangzsx.news_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fangzsx.news_app.R
import com.fangzsx.news_app.databinding.FragmentSearchNewsBinding
import com.fangzsx.news_app.ui.NewsActivity
import com.fangzsx.news_app.util.Resource
import com.fangzsx.news_app.viewmodels.NewsViewModel


class SearchNewsFragment : Fragment() {

    private lateinit var binding : FragmentSearchNewsBinding
    lateinit var viewModel : NewsViewModel

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


        viewModel.searchNewsResult.observe(viewLifecycleOwner){ response ->

            when(response){
                is Resource.Success -> {
                    //hide progressbar

                }
            }

        }

    }

}