package com.riezki.vixnewsapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.model.response.ArticlesItem

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInsert(article: ArticlesItem): Long

    @Query("SELECT * FROM news")
    fun getAllArticles(): LiveData<List<ArticlesItem>>

    @Delete
    suspend fun deleteArticle(article: ArticlesItem)
}