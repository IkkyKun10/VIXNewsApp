package com.riezki.vixnewsapp.data.remote.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.riezki.vixnewsapp.data.remote.retrofit.ApiConfig
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.model.response.NewsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ArticleDataSource(private val scope: CoroutineScope) : PageKeyedDataSource<Int, ArticlesItem>() {

    val breakingNews: MutableLiveData<List<ArticlesItem>?> = MutableLiveData()
    val breakingPageNumber = 1
    val breakingNewsResponse: NewsResponse? = null



    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
        try {
            scope.launch {
                val response = ApiConfig.apiService().getHeadlineNews("id", params.requestedLoadSize)
                when {
                    response.isSuccessful -> {
                        response.body()?.articles.let {
                            callback.onResult(it!!, params.key+1)
                        }
                    }
                }
            }
        }catch (ex: Exception) {
            Log.e("ArticleDataSource: ", ex.message.toString())
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ArticlesItem>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ArticlesItem>) {
        scope.launch {
            try {
                val response = ApiConfig.apiService().getHeadlineNews("id", 1)
                when {
                    response.isSuccessful -> {
                        response.body()?.articles.let {
                            breakingNews.postValue(it)
                            callback.onResult(it!!, previousPageKey = null, nextPageKey = 2)
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("ArticleDataSource: ", ex.message.toString())
            }
        }
    }
}