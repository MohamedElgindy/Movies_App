package com.example.movies_app.models

data class ActorDetailsMovieCreditModel(
    val cast: List<CastX>,
    val crew: List<CrewX>,
    val id: Int
)