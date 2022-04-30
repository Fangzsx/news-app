package com.fangzsx.news_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fangzsx.news_app.databinding.NewsItemLayoutBinding
import com.fangzsx.news_app.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    var isSaveVisible : Boolean = true

    inner class ArticleViewHolder(val binding : NewsItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            NewsItemLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.binding.apply {
            tvTitle.text = "\"${article.title}\""
            tvDescription.text = article.description
            ivImage.load(article.urlToImage){
                crossfade(true)
                crossfade(1000)
            }

            btnReadMore.setOnClickListener {
                onReadMoreClickListener?.let {
                    it(article)
                }
            }

            if(isSaveVisible){
                ivSave.setOnClickListener {
                    onSaveClickListener?.let {
                        it(article)
                    }
                }
            }else{
                ivSave.visibility = View.GONE
            }



        }

    }


    private var onReadMoreClickListener : ((Article) -> Unit)? = null

    private var onSaveClickListener : ((Article) -> Unit)? = null

    fun setOnReadMoreClickListener(listener : (Article) -> Unit){
        onReadMoreClickListener = listener
    }

    fun setOnSaveClickListener(listener : (Article) -> Unit){
        onSaveClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}