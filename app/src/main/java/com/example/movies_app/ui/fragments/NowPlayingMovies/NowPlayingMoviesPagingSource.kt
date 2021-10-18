package com.example.movies_app.ui.fragments.NowPlayingMovies

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies_app.api.RetrofitInstance
import com.example.movies_app.models.ResultX
import com.example.movies_app.ui.fragments.TopRatedMovies.NETWORK_PAGE_SIZE
import com.example.movies_app.util.api_key
import retrofit2.HttpException
import java.io.IOException

private const val TMDB_STARTING_PAGE_INDEX = 1


class NowPlayingMoviesPagingSource : PagingSource<Int, ResultX>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultX> {
        val pageIndex = params.key ?: TMDB_STARTING_PAGE_INDEX

        return try {
            val response = RetrofitInstance.api.getNowPlayingMoviesMovies(
                api_key,
                pageNumber = pageIndex
            )

            val movies = response.body()!!.results
            val nextKey =
                if (movies.isEmpty()) {
                    null
                } else {
                    // By default, initial load size = 3 * NETWORK PAGE SIZE
                    // ensure we're not requesting duplicating items at the 2nd request
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex == TMDB_STARTING_PAGE_INDEX) null else pageIndex,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultX>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
