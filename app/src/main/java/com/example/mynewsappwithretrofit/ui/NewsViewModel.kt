package com.example.mynewsappwithretrofit.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mynewsappwithretrofit.model.Article
import com.example.mynewsappwithretrofit.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun getBreakingNews(
        countryCode: String,
        searchQuery: String? = null
    ): Flow<PagingData<Article>> {
        return repository.getBreakingNews(countryCode, searchQuery).cachedIn(viewModelScope)
    }

    fun searchNews(query: String): Flow<PagingData<Article>> {
        return repository.searchNews(query).cachedIn(viewModelScope)
    }

    // حفظ المقال
    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsertArticle(article)
    }

    fun getSavedArticles() = repository.getSavedArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

}
