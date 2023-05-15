package com.riezki.vixnewsapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import kotlinx.coroutines.launch

class DetailNewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val newsData = MutableLiveData<NewsEntity>()

    fun setNewsData(news: NewsEntity) {
        newsData.value = news
    }

    val bookmarkStatus: LiveData<Boolean> = newsData.switchMap {
        repository.isNewsBookmarked(it.title)
    }

    fun changeBookmark(newsDetail: NewsEntity) {
        viewModelScope.launch {
            if (bookmarkStatus.value as Boolean) {
                repository.deleteNews(newsDetail.title)
            } else {
                repository.saveNews(newsDetail)
            }
        }
    }
}