package com.example.mynewsappwithretrofit.db

import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context
    ): ArticleDatabase =
        Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            "article_db"
        ).build()

    @Provides
    @Singleton
    fun provideArticleDao(db: ArticleDatabase): ArticleDao = db.getArticleDao()
}
