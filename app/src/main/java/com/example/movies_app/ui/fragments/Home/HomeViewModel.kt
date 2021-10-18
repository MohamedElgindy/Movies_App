package com.example.movies_app.ui.fragments.Home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.movies_app.models.NowPlayingMoviesModel
import com.example.movies_app.models.PopularMoviesModel
import com.example.movies_app.models.TopRatedMoviesModel
import com.example.movies_app.models.UpComingMoviesModel
import com.example.movies_app.repository.HomeRepository
import com.example.movies_app.util.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val homeRepository = HomeRepository()

    private val _popularMovieList = MutableLiveData<State<Response<PopularMoviesModel>>>()
    val popularMovieList = _popularMovieList

    private val _nowPlayingMovieList = MutableLiveData<State<Response<NowPlayingMoviesModel>>>()
    val nowPlayingMovieList = _nowPlayingMovieList

    private val _upComingMoviesList = MutableLiveData<State<Response<UpComingMoviesModel>>>()
    val upComingMoviesList = _upComingMoviesList

    private val _topRatedMoviesList = MutableLiveData<State<Response<TopRatedMoviesModel>>>()
    val topRatedMoviesList = _topRatedMoviesList



    init {
        loadHomeScreenMovies()
    }
    fun reload(){
        loadHomeScreenMovies()
    }


    private fun loadHomeScreenMovies() {
        viewModelScope.launch {
            homeRepository.getUpComingMovies().collect { _upComingMoviesList.value = it }
            homeRepository.getPopularMovies().collect { _popularMovieList.value = it }
            homeRepository.getTopRatedMovies().collect { _topRatedMoviesList.value = it }
            homeRepository.getNowPlayingMovies().collect { _nowPlayingMovieList.value = it }
        }
    }



}