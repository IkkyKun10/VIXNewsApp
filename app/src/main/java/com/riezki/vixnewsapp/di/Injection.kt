package com.riezki.vixnewsapp.di

import android.content.Context
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.data.local.room.NewsDatabase
import com.riezki.vixnewsapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.apiService()
        val database = NewsDatabase.getIntance(context)
        return NewsRepository(apiService, database)
    }
}
