package com.riezki.vixnewsapp.data

import com.riezki.vixnewsapp.data.local.room.NewsDatabase
import com.riezki.vixnewsapp.data.remote.retrofit.ApiConfig
import com.riezki.vixnewsapp.model.response.ArticlesItem

class NewsRepository(private val database: NewsDatabase) {

    suspend fun getHeadlineNews(countryCode: String, pageNumber: Int) =
        ApiConfig.apiService().getHeadlineNews(countryCode, pageNumber)

    suspend fun updateInsert(article: ArticlesItem) = database.newsDao().updateInsert(article)

    fun getSavedNews() = database.newsDao().getAllArticles()

    suspend fun deleteArticle(article: ArticlesItem) = database.newsDao().deleteArticle(article)
}