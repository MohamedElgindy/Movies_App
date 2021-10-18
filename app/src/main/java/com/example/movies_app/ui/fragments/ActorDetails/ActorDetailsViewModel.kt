package com.example.movies_app.ui.fragments.ActorDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.models.ActorDetailsModel
import com.example.movies_app.models.ActorDetailsMovieCreditModel
import com.example.movies_app.repository.MovieDetailsRepository
import com.example.movies_app.util.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response

class ActorDetailsViewModel : ViewModel() {

    var actorIdVar: Int = 0
    private val movieDetailsRepository = MovieDetailsRepository()
    private val _actorDetails = MutableLiveData<State<Response<ActorDetailsModel>>>()
    val actorDetails = _actorDetails
    private val _actorMovieCredit = MutableLiveData<State<Response<ActorDetailsMovieCreditModel>>>()
    val actorMovieCredit = _actorMovieCredit
    fun getActorDetails(actorId: Int) {
        actorIdVar = actorId
        loadActorDetails(actorId)
    }

    fun reload(){
        loadActorDetails(actorIdVar)
    }

    private fun loadActorDetails(actorId: Int) {
        viewModelScope.launch {
            movieDetailsRepository.getActorDetails(actorId).collect { _actorDetails.value = it }

            movieDetailsRepository.getActorMovieCredit(actorId)
                .collect { _actorMovieCredit.value = it }
        }
    }

}