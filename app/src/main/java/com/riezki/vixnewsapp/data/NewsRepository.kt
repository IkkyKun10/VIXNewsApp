package com.riezki.vixnewsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.data.local.room.NewsDatabase
import com.riezki.vixnewsapp.data.remote.datasource.NewsPagingSource
import com.riezki.vixnewsapp.data.remote.retrofit.ApiService
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Resource

class NewsRepository(private val apiService: ApiService, private val database: NewsDatabase) {

    fun getHeadlineNews() : LiveData<PagingData<ArticlesItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                NewsPagingSource(apiService)
            }
        ).liveData
    }

    fun getFirstHeadlineNews(countryCode: String) : LiveData<Resource<List<ArticlesItem>>> = liveData {
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

    suspend fun saveNews(newsEntity: NewsEntity) {
        database.newsDao().saveNews(newsEntity)
    }

    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return database.newsDao().getAllBookmarked()
    }

    suspend fun deleteNews(title: String) {
        database.newsDao().deleteNews(title)
    }

    fun isNewsBookmarked(title: String) : LiveData<Boolean> {
        return database.newsDao().isNewsBookmarked(title)
    }
}