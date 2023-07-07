package com.riezki.vixnewsapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "news_entity")
@Parcelize
data class NewsEntity(

    @field:PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Int? = null,

    @field:ColumnInfo(name = "title")
    val title: String? = null,

    @field:ColumnInfo(name = "publishedAt")
    val publishedAt: String? = null,

    @field:ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null,

    @field:ColumnInfo(name = "author")
    val author: String? = null,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean? = false
) : Parcelable
