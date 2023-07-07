package com.riezki.vixnewsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {

    private val news = MutableLiveData<NewsEntity>()

    fun headlineNewsData() : LiveData<PagingData<NewsEntity>> = repository.getHeadlineNews().cachedIn(viewModelScope)

    fun getFirstTopHeadlineNews(countryCode: String) : LiveData<Resource<List<ArticlesItem>>> =
        repository.getFirstHeadlineNews(countryCode)

    fun setCheckingData(newsEntity: NewsEntity) {
        news.value = newsEntity
    }

    private val bookmarkStatus: LiveData<Boolean> = news.switchMap { newsLocal ->
        newsLocal.title?.let { repository.isNewsBookmarked(it) }
    }

    fun changeBookmark(newsEntity: NewsEntity) {
        viewModelScope.launch {
            if (bookmarkStatus.value as Boolean) {
                newsEntity.title?.let { repository.deleteNews(it) }
            } else {
                repository.saveNews(newsEntity)
            }
        }
    }

    fun saveNews(newsEntity: NewsEntity) {
        viewModelScope.launch {
            repository.updateInPaging(newsEntity, true)
        }
    }

    fun deleteNews(newsEntity: NewsEntity) {
        viewModelScope.launch {
            repository.updateInPaging(newsEntity, false)
        }
    }
}