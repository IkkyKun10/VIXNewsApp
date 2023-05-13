package com.riezki.vixnewsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.riezki.vixnewsapp.data.local.room.NewsDatabase
import com.riezki.vixnewsapp.data.remote.datasource.NewsPagingSource
import com.riezki.vixnewsapp.data.remote.retrofit.ApiConfig
import com.riezki.vixnewsapp.data.remote.retrofit.ApiService
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Resource

class NewsRepository(private val apiService: ApiService, private val database: NewsDatabase) {

    fun getHeadlineNews() : LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                NewsPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getFirstHeadlineNews(countryCode: String) : LiveData<Resource<List<ArticlesItem>>> = liveData {
        emit(Resource.Loading(null))
        try {
            val response = apiService.getFirstHealineNews(countryCode)
            val list = response.articles
            emit(Resource.Success(list))
        } catch (e: Exception) {
            Log.d("NewsRepository", "getFirstHeadlineNews: ${e.message.toString()} ")
            emit(Resource.Error(e.hashCode(), e.message.toString()))
        }
    }

    //suspend fun updateInsert(article: ArticlesItem) = database.newsDao().updateInsert(article)

    //fun getSavedNews() = database.newsDao().getAllArticles()

    //suspend fun deleteArticle(article: ArticlesItem) = database.newsDao().deleteArticle(article)
}