package com.example.movies_app.models

data class PopularMoviesModel(
    val page: Int,
    val results: List<MoviesModel>,
    val total_pages: Int,
    val total_results: Int
)