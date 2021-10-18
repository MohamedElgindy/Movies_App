package com.example.movies_app.models

data class TopRatedMoviesModel(
    val page: Int,
    val results: List<ResultXX>,
    val total_pages: Int,
    val total_results: Int
)