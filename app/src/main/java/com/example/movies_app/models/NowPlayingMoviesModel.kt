package com.example.movies_app.models

data class NowPlayingMoviesModel(
    val dates: DatesX,
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)