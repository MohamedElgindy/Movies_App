package com.example.movies_app.models

data class SearchMoviesModel(
    val page: Int,
    val results: List<ResultXXXX>,
    val total_pages: Int,
    val total_results: Int
)