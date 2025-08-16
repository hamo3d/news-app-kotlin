package com.example.mynewsappwithretrofit.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynewsappwithretrofit.model.Article


@Database(entities = [Article::class], version = 1, exportSchema = false)
@androidx.room.TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}
