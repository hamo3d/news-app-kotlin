package com.example.mynewsappwithretrofit.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mynewsappwithretrofit.db.ArticleDatabase
import com.example.mynewsappwithretrofit.model.Article
import com.example.mynewsappwithretrofit.retrofit_api.NewsAPI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val db: ArticleDatabase,
    private val api: NewsAPI
) {
    fun getBreakingNews(
        countryCode: String,
        searchQuery: String? = null
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArticlePagingSource(api, countryCode, searchQuery) }
        ).flow
    }

    fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(api, query) }
        ).flow
    }


    suspend fun upsertArticle(article: Article) = db.getArticleDao().upsert(article)
    fun getSavedArticles() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}
