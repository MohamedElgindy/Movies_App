package com.example.movies_app.repository

import com.example.movies_app.api.RetrofitInstance
import com.example.movies_app.util.State
import com.example.movies_app.util.api_key
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepository {

    fun getPopularMovies() = flow {

        // Emit loading state
        emit(State.loading())
        val popularMoviesResponse = RetrofitInstance.api.getPopularMovies(api_key)
        emit(State.success(popularMoviesResponse))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getTopRatedMovies() = flow {

        // Emit loading state
        emit(State.loading())
        val popularMoviesResponse = RetrofitInstance.api.getTopRatedMovies(api_key, 1)
        emit(State.success(popularMoviesResponse))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getUpComingMovies() = flow {

        // Emit loading state
        emit(State.loading())
        val popularMoviesResponse = RetrofitInstance.api.getUpcomingMovies(api_key, 1)
        emit(State.success(popularMoviesResponse))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getNowPlayingMovies() = flow {

        // Emit loading state
        emit(State.loading())
        val popularMoviesResponse = RetrofitInstance.api.getNowPlayingMoviesMovies(api_key, 1)
        emit(State.success(popularMoviesResponse))

    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}