package com.riezki.vixnewsapp.ui.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

class BookmarkViewModel(private val repository: NewsRepository) : ViewModel() {


    fun saveNews(newsEntity: NewsEntity) {
        viewModelScope.launch {
            repository.saveNews(newsEntity)
        }
    }

    fun deleteNews(newsEntity: NewsEntity) {
        viewModelScope.launch {
            repository.deleteNews(newsEntity.title)
        }
    }

    fun getBookmarkedNews() : LiveData<List<NewsEntity>> {
        return repository.getBookmarkedNews()
    }
}