package com.riezki.vixnewsapp.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.ui.bookmarks.BookmarkViewModel
import com.riezki.vixnewsapp.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val app: Application, private val newsRepository: NewsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(app, newsRepository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(app, newsRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}