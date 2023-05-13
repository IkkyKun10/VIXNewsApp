package com.riezki.vixnewsapp.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.riezki.vixnewsapp.data.remote.retrofit.ApiService
import com.riezki.vixnewsapp.model.response.ArticlesItem
import retrofit2.HttpException

class NewsPagingSource(private val apiService: ApiService) : PagingSource<Int, ArticlesItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }



    override fun getRefreshKey(state: PagingState<Int, ArticlesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticlesItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getHeadlineNews("us", pageNumber = position, pageSize = params.loadSize)

            LoadResult.Page(
                data = responseData.articles,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position-1,
                nextKey = if (responseData.articles.isEmpty()) null else position+1,
            )

        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}