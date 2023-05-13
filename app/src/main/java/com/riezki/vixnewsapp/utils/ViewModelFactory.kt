package com.riezki.vixnewsapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.di.Injection
import com.riezki.vixnewsapp.ui.bookmarks.BookmarkViewModel
import com.riezki.vixnewsapp.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val newsRepository: NewsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(newsRepository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(newsRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) : ViewModelFactory {
            return ViewModelFactory(
                Injection.provideRepository(context)
            )
        }
    }
}