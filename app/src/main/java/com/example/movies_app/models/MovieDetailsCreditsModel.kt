package com.example.movies_app.models

data class MovieDetailsCreditsModel(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)