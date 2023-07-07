package com.riezki.vixnewsapp.data.remote.datasource

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.data.local.room.NewsDatabase
import com.riezki.vixnewsapp.data.local.room.RemoteKeys
import com.riezki.vixnewsapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val apiService: ApiService,
    private val database: NewsDatabase
) : RemoteMediator<Int, NewsEntity>() {
    //WARNINGGG!!!
    //change class newsEntitiy to ArticleItems
    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, NewsEntity>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKeys = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKeys
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKeys = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKeys
            }
        }

        try {
            val responseData = apiService.getHeadlineNews(pageNumber = page, pageSize = state.config.pageSize)
            val endOfPaginationReached = responseData.articles.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.newsDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = responseData.articles.map {
                    RemoteKeys(
                        id = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                    )
                }

                val newData = responseData.articles.map {
                    //val isNewsBookmarked = database.newsDao().isBookmarkedInList(it.title.toString())
                    NewsEntity(
                        title = it.title,
                        urlToImage = it.urlToImage,
                        publishedAt = it.publishedAt,
                        id = it.id,
                        url = it.url,
                        author = it.author,
                        //isBookmarked = isNewsBookmarked,
                    )
                }

                database.remoteKeysDao().insertAll(keys)
                database.newsDao().insertAllToLocal(news = newData)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("StoryRemoteMediator", "load: ${e.message.toString()} ")
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, NewsEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            data.id?.let {
                database.remoteKeysDao().getRemoteKeyId(it)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, NewsEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            data.id?.let { id ->
                database.remoteKeysDao().getRemoteKeyId(id)
            }
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, NewsEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeyId(id)
            }
        }
    }
}