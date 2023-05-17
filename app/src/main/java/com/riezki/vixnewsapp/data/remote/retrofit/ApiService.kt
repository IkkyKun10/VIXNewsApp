package com.riezki.vixnewsapp.data.remote.retrofit

import com.riezki.vixnewsapp.BuildConfig
import com.riezki.vixnewsapp.model.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    suspend fun getHeadlineNews(
        @Query("q") searchIn: String? = "tesla",
        @Query(PAGE) pageNumber: Int = 1,
        @Query(SIZE) pageSize: Int = 5,
        @Query(API_KEYS) apiKey: String = BuildConfig.API_KEY
    ) : NewsResponse

    @GET("top-headlines")
    suspend fun getFirstHealineNews(
        @Query(COUNTRY) countryCode: String = "us",
        @Query(PAGE) pageNumber: Int = 1,
        @Query(SIZE) pageSize: Int = 5,
        @Query(API_KEYS) apiKey: String = BuildConfig.API_KEY
    ) : NewsResponse

    companion object {
        const val COUNTRY = "country"
        const val PAGE = "page"
        const val SIZE = "pageSize"
        const val API_KEYS = "apiKey"
    }
}