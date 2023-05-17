package com.riezki.vixnewsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Resource

class HomeViewModel(private val repository: NewsRepository) : ViewModel() {

    val headlineNewsData : LiveData<PagingData<ArticlesItem>> =
        repository.getHeadlineNews().cachedIn(viewModelScope)

    fun getFirstHeadlineNews(countryCode: String) : LiveData<Resource<List<ArticlesItem>>> =
        repository.getFirstHeadlineNews(countryCode)

}