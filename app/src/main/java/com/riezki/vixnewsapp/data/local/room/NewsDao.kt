package com.riezki.vixnewsapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.model.response.ArticlesItem

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNews(news: NewsEntity)

    @Query("SELECT * FROM news_entity")
    fun getAllBookmarked(): LiveData<List<NewsEntity>>

    @Query("DELETE FROM news_entity WHERE title = :newsTitle")
    suspend fun deleteNews(newsTitle: String)

    @Query("SELECT EXISTS(SELECT * FROM news_entity WHERE title = :title)")
    fun isNewsBookmarked(title: String): LiveData<Boolean>
}