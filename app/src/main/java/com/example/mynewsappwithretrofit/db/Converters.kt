package com.example.mynewsappwithretrofit.db

// Converters.kt
import androidx.room.TypeConverter
import com.example.mynewsappwithretrofit.model.Source
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromSource(source: Source?): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String): Source {
        return Gson().fromJson(sourceString, Source::class.java)
    }
}
