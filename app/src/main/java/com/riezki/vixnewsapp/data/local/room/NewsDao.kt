package com.riezki.vixnewsapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.riezki.vixnewsapp.data.local.entity.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewsInDetail(news: NewsEntity)

    @Query("SELECT * FROM news_entity WHERE bookmarked = 1")
    fun getAllBookmarked(): LiveData<List<NewsEntity>>

    @Query("DELETE FROM news_entity WHERE title = :newsTitle AND bookmarked = 1")
    suspend fun deleteNewsInDetail(newsTitle: String)

    @Query("SELECT EXISTS(SELECT * FROM news_entity WHERE title = :title AND bookmarked = 1)")
    fun isNewsBookmarked(title: String): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT * FROM news_entity WHERE title = :title AND bookmarked = 1)")
    fun isBookmarkedInList(title: String) : Boolean

    @Query("DELETE FROM news_entity WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Update
    suspend fun updateNewsInPaging(newsEntity: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllToLocal(news: List<NewsEntity>)

    @Query("SELECT * FROM news_entity")
    fun getAllNews() : PagingSource<Int, NewsEntity>
}