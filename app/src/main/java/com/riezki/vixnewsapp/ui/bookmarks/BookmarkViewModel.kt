package com.riezki.vixnewsapp.ui.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.model.response.ArticlesItem
import kotlinx.coroutines.launch

class BookmarkViewModel(val app: Application, val repository: NewsRepository) : AndroidViewModel(app) {

    fun getSavedNews() : LiveData<List<ArticlesItem>> {
        return repository.getSavedNews()
    }

    fun deteleArticle(articlesItem: ArticlesItem) =
        viewModelScope.launch {
            repository.deleteArticle(articlesItem)
        }

    fun saveNews(articlesItem: ArticlesItem) =
        viewModelScope.launch {
            repository.updateInsert(articlesItem)
        }
}