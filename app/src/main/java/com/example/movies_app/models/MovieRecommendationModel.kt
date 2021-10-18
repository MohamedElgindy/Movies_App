package com.example.movies_app.models

data class MovieRecommendationModel(
    val page: Int,
    val results: List<ResultXXX>,
    val total_pages: Int,
    val total_results: Int
)