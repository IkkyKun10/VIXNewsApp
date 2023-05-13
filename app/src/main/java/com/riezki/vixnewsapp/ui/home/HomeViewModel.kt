package com.riezki.vixnewsapp.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.riezki.vixnewsapp.NewsApplication
import com.riezki.vixnewsapp.data.NewsRepository
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.model.response.NewsResponse
import com.riezki.vixnewsapp.utils.Resource
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class HomeViewModel(val app: Application, private val repository: NewsRepository) : AndroidViewModel(app) {

    val headlineNewsData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinePageNumber = 1
    var headlineNewsResponse: NewsResponse? = null

    init {
        getHeadlineNews("us")
    }

    fun getHeadlineNews(countryCode: String) = viewModelScope.launch {
        headlineNewsCall(countryCode)
    }

    fun saveNews(articlesItem: ArticlesItem) =
        viewModelScope.launch {
            repository.updateInsert(articlesItem)
        }

    private suspend fun headlineNewsCall(countryCode: String) {
        headlineNewsData.value = Resource.Loading()
        try {
            val response = repository.getHeadlineNews(countryCode, headlinePageNumber)
            headlineNewsData.postValue(handleHeadlineNewsResponse(response))

        } catch (e: Exception) {
            when (e) {
                is IOException -> headlineNewsData.postValue(Resource.Error(e.hashCode(), "Network Failure"))
                else -> headlineNewsData.postValue(Resource.Error(e.hashCode(), "Conversion Error"))
            }
        }
    }

    private fun handleHeadlineNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinePageNumber++
                if (headlineNewsResponse == null) {
                    headlineNewsResponse = resultResponse
                } else {
                    val oldArticles = headlineNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles!!)
                }
                return Resource.Success(headlineNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.code(), response.message())
    }

    private fun haveInternet(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}