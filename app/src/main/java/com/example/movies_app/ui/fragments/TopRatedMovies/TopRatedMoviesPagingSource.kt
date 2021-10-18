package com.example.movies_app.ui.fragments.TopRatedMovies
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movies_app.api.RetrofitInstance
import com.example.movies_app.models.ResultXX
import com.example.movies_app.util.api_key
import retrofit2.HttpException
import java.io.IOException

private const val TMDB_STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 25


class TopRatedMoviesPagingSource: PagingSource<Int, ResultXX>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultXX>{
        val pageIndex = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val response = RetrofitInstance.api.getTopRatedMovies(
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
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResultXX>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}
