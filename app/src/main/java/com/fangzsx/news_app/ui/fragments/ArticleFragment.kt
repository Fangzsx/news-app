package com.fangzsx.news_app.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fangzsx.news_app.databinding.FragmentArticleBinding


class ArticleFragment : Fragment() {

    private lateinit var binding : FragmentArticleBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments

        if(bundle != null){

            binding.apply {
                webView.apply {
                    webViewClient = WebViewClient()
                    bundle.getString("webview_url")?.let {
                        loadUrl(it)
                    }
                    
                }

                tvSource.text = "Source: ${bundle.getString("source")}"

            }

        }
    }
}