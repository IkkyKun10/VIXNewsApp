package com.riezki.vixnewsapp.ui.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.data.local.entity.NewsEntity

class BookmarkViewModel(private val repository: NewsRepository) : ViewModel() {

    fun getBookmarkedNews() : LiveData<List<NewsEntity>> {
        return repository.getBookmarkedNews()
    }
}