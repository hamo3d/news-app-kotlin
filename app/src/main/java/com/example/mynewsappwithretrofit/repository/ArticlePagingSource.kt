package com.example.mynewsappwithretrofit.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mynewsappwithretrofit.model.Article
import com.example.mynewsappwithretrofit.retrofit_api.NewsAPI

class ArticlePagingSource(
    private val api: NewsAPI,
    private val countryCode: String? = null,
    private val searchQuery: String? = null
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val response = if (!searchQuery.isNullOrEmpty()) {
                // طلب البحث
                api.searchForNews(searchQuery, page)
            } else {
                // طلب الأخبار العادية حسب الدولة
                api.getBreakingNews(countryCode ?: "us", page)
            }
            val articles = response.body()?.articles ?: emptyList()

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
