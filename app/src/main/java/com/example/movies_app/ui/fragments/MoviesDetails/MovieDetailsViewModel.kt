package com.example.movies_app.ui.fragments.MoviesDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.models.MovieDetailsCreditsModel
import com.example.movies_app.models.MovieDetailsModel
import com.example.movies_app.models.MovieRecommendationModel
import com.example.movies_app.repository.MovieDetailsRepository
import com.example.movies_app.util.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {
    private val movieDetailsRepository = MovieDetailsRepository()

    private val _movieDetails = MutableLiveData<State<Response<MovieDetailsModel>>>()
    val movieDetails = _movieDetails

    private val _movieDetailsCredit = MutableLiveData<State<Response<MovieDetailsCreditsModel>>>()
    val movieDetailsCredit = _movieDetailsCredit

    var movieIdVar = 0
    private val _movieDetailsRecommendation =
        MutableLiveData<State<Response<MovieRecommendationModel>>>()
    val movieRecommendation = _movieDetailsRecommendation

    fun getMovieDetails(movieId: Int) {
        movieIdVar = movieId
        loadMovieDetailsMovies(movieId)
    }


    private fun loadMovieDetailsMovies(movieId: Int) {
        viewModelScope.launch {
            movieDetailsRepository.getMovieDetailsCredit(movieId)
                .collect { _movieDetailsCredit.value = it }
            viewModelScope.launch {
                movieDetailsRepository.getMovieDetails(movieId).collect { _movieDetails.value = it }
            }
            movieDetailsRepository.getMovieRecommendation(movieId)
                .collect {
                    _movieDetailsRecommendation.value = it

                }
        }
    }

    fun reload() {
        loadMovieDetailsMovies(movieIdVar)
    }

}