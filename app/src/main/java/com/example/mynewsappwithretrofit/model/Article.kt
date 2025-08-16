package com.example.mynewsappwithretrofit.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@SuppressLint("ParcelCreator")

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val source: Source? = null,
    val title: String? = null,
    val url: String? = null,
    val urlToImage: String? = null
) : Serializable