package com.riezki.vixnewsapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.riezki.vixnewsapp.data.local.Converters
import com.riezki.vixnewsapp.data.local.entity.NewsEntity

@Database(entities = [NewsEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao() : NewsDao
    abstract fun remoteKeysDao() : RemoteKeyDao

    companion object {
        @Volatile
        private var instance: NewsDatabase? = null

        @JvmStatic
        fun getIntance(context: Context) : NewsDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java, "News.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}