package com.riezki.vixnewsapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riezki.vixnewsapp.data.local.Converters
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.model.response.ArticlesItem

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao() : NewsDao

    companion object {
        @Volatile
        private var instance: NewsDatabase? = null
        fun getIntance(context: Context) : NewsDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java, "News.db"
                ).build()
            }
        }
    }
}