package com.example.movies_app.ui.fragments.SearchFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.models.SearchMoviesModel
import com.example.movies_app.repository.MovieDetailsRepository
import com.example.movies_app.util.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response


class SearchViewModel : ViewModel() {
    private val movieDetailsRepository = MovieDetailsRepository()

    private val _searchMovieList = MutableLiveData<State<Response<SearchMoviesModel>>>()
    val searchMovieList = _searchMovieList

    fun getSearchResult(movieName: String) {
        loadSearchMovies(movieName)
    }

    private fun loadSearchMovies(movieName: String) {
        viewModelScope.launch {
            movieName.let { query ->
                movieDetailsRepository.getSearchMoviesResult(query)
                    .collect { _searchMovieList.value = it }
            }
        }
    }
}