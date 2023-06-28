package com.riezki.vixnewsapp.data.remote.datasource

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
class NewsRemoteMediator(private val apiService: ApiService, private val database: NewsDatabase) : RemoteMediator<Int, NewsEntity>() {
    //WARNINGGG!!!
    //change class newsEntitiy to ArticleItems
    private companion object {
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

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
        }

        try {
            val responseData = apiService.getHeadlineNews(pageSize = state.config.pageSize)
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
                        id = it.id!!,
                        prevKey = prevKey!!,
                        nextKey = nextKey!!
                    )
                }

                val newData = responseData.articles.map {
                    val isNewsBookmarked = database.newsDao().isBookmarkedInList(it.title.toString())
                    NewsEntity(
                        title = it.title.toString(),
                        urlToImage = it.urlToImage.toString(),
                        publishedAt = it.publishedAt.toString(),
                        id = it.id,
                        url = it.url,
                        author = it.author,
                        isBookmarked = isNewsBookmarked,
                    )
                }

                database.remoteKeysDao().insertAll(keys)
                database.newsDao().insertAllToLocal(news = newData)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, NewsEntity>) : RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeyId(data.id!!)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, NewsEntity>) : RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            data.id?.let { id ->
                database.remoteKeysDao().getRemoteKeyId(id)
            }
        }
    }

    private suspend fun getRemoteKeysClosestToCurrentPosition(state: PagingState<Int, NewsEntity>) : RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeyId(id)
            }
        }
    }
}