package com.example.movies_app.models

data class UpComingMoviesModel(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)