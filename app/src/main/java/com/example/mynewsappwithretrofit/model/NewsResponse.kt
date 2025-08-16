package com.example.mynewsappwithretrofit.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)