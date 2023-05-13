package com.riezki.vixnewsapp.ui.bookmarks

import androidx.lifecycle.ViewModel
import com.riezki.vixnewsapp.data.NewsRepository

class BookmarkViewModel(val repository: NewsRepository) : ViewModel() {

//    fun getSavedNews() : LiveData<List<ArticlesItem>> {
//        return repository.getSavedNews()
//    }
//
//    fun deteleArticle(articlesItem: ArticlesItem) =
//        viewModelScope.launch {
//            repository.deleteArticle(articlesItem)
//        }
//
//    fun saveNews(articlesItem: ArticlesItem) =
//        viewModelScope.launch {
//            repository.updateInsert(articlesItem)
//        }
}