package com.riezki.vixnewsapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_entity")
data class NewsEntity(
    @field:ColumnInfo(name = "title")
    @field:PrimaryKey
    val title: String,

    @field:ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @field:ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null,

    val author: String? = null,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
)
