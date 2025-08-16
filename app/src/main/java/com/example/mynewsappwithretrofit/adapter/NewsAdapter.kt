package com.example.mynewsappwithretrofit.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewsappwithretrofit.databinding.ItemArticlePreviewBinding
import com.example.mynewsappwithretrofit.model.Article

class NewsAdapter(
    private val onItemClick: (Article) -> Unit
) : PagingDataAdapter<Article, NewsAdapter.ArticleViewHolder>(ARTICLE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article?) {
            binding.apply {
                tvTitle.text = article?.title
                tvDescription.text = article?.description
                tvSource.text = article?.source?.name
                tvPublishedAt.text = article?.publishedAt
                Glide.with(ivArticleImage.context)
                    .load(article?.urlToImage)
                    .into(ivArticleImage)

                root.setOnClickListener {
                    article?.let {
                        onItemClick(it)
                    }
                }
            }
        }
    }

    companion object {
        private val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: Article, newItem: Article) =
                oldItem == newItem
        }
    }
}
