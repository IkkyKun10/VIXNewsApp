package com.riezki.vixnewsapp.data.remote.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.riezki.vixnewsapp.model.response.ArticlesItem
import kotlinx.coroutines.CoroutineScope

class ArticleDataSourceFactory(private val scope: CoroutineScope) : DataSource.Factory<Int, ArticlesItem>() {

    private val articleDataSourceLiveData = MutableLiveData<ArticleDataSource>()

    override fun create(): DataSource<Int, ArticlesItem> {
        val newsArticleDataSource = ArticleDataSource(scope)
        articleDataSourceLiveData.postValue(newsArticleDataSource)
        return newsArticleDataSource
    }
}