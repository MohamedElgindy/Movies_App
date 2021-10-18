package com.example.movies_app.repository

import com.example.movies_app.api.RetrofitInstance
import com.example.movies_app.util.State
import com.example.movies_app.util.api_key
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieDetailsRepository {

    fun getMovieDetails(movieId: Int) = flow {
        // Emit loading state
        emit(State.loading())
        val movieDetails = RetrofitInstance.api.getMovieDetail(id = movieId, key = api_key)
        emit(State.success(movieDetails))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getMovieDetailsCredit(movieId: Int) = flow {
        // Emit loading state
        emit(State.loading())
        val movieDetails = RetrofitInstance.api.getMovieDetailCredit(id = movieId, key = api_key)
        emit(State.success(movieDetails))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getMovieRecommendation(movieId: Int) = flow {
        // Emit loading state
        emit(State.loading())
        val movieDetails =
            RetrofitInstance.api.getMovieDetailRecommendations(id = movieId, key = api_key)
        emit(State.success(movieDetails))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getActorDetails(actorId: Int) = flow {
        // Emit loading state
        emit(State.loading())
        val movieDetails = RetrofitInstance.api.getActorDetail(id = actorId, key = api_key)
        emit(State.success(movieDetails))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getActorMovieCredit(actorId: Int) = flow {
        // Emit loading state
        emit(State.loading())
        val movieDetails = RetrofitInstance.api.getActorMovieCredit(id = actorId, key = api_key)
        emit(State.success(movieDetails))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getSearchMoviesResult(movieName: String) = flow {
        // Emit loading state
        emit(State.loading())
        val movieDetails = RetrofitInstance.api.getSearchMovie(search = movieName, key = api_key)
        emit(State.success(movieDetails))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}