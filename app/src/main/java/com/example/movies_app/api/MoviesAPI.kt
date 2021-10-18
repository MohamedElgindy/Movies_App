package com.example.movies_app.api

import com.example.movies_app.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesAPI {
    companion object {
        const val POPULAR_MOVIES = "/3/movie/popular"
        const val NOW_PLAYING_MOVIES = "/3/movie/now_playing"
        const val TOP_RATED_MOVIES = "/3/movie/top_rated"
        const val UPCOMING_MOVIES = "/3/movie/upcoming"
        const val MOVIE_DETAILS = "/3/movie/{movie_id}"
        const val MOVIE_DETAILS_CREDITS = "/3/movie/{movie_id}/credits"
        const val MOVIE_DETAILS_RECOMMENDATIONS = "/3/movie/{movie_id}/recommendations"
        const val ACTOR_DETAILS = "/3/person/{person_id}"
        const val ACTOR_MOVIES_CREDIT = "/3/person/{person_id}/movie_credits"
        const val SEARCH_MOVIE = "3/search/movie"

    }

    @GET(POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query("api_key") ApiKey: String):
            Response<PopularMoviesModel>

    @GET(NOW_PLAYING_MOVIES)
    suspend fun getNowPlayingMoviesMovies(
        @Query("api_key") ApiKey: String,
        @Query("page") pageNumber: Int
    ):
            Response<NowPlayingMoviesModel>

    @GET(TOP_RATED_MOVIES)
    suspend fun getTopRatedMovies(
        @Query("api_key") ApiKey: String,
        @Query("page") pageNumber: Int
    ):
            Response<TopRatedMoviesModel>

    @GET(UPCOMING_MOVIES)
    suspend fun getUpcomingMovies(
        @Query("api_key") ApiKey: String,
        @Query("page") pageNumber: Int
    ):
            Response<UpComingMoviesModel>

    @GET(MOVIE_DETAILS)
    suspend fun getMovieDetail(@Path("movie_id") id: Int, @Query("api_key") key: String):
            Response<MovieDetailsModel>

    @GET(MOVIE_DETAILS_CREDITS)
    suspend fun getMovieDetailCredit(@Path("movie_id") id: Int, @Query("api_key") key: String):
            Response<MovieDetailsCreditsModel>

    @GET(MOVIE_DETAILS_RECOMMENDATIONS)
    suspend fun getMovieDetailRecommendations(
        @Path("movie_id") id: Int,
        @Query("api_key") key: String
    ):
            Response<MovieRecommendationModel>

    @GET(ACTOR_DETAILS)
    suspend fun getActorDetail(@Path("person_id") id: Int, @Query("api_key") key: String):
            Response<ActorDetailsModel>

    @GET(ACTOR_MOVIES_CREDIT)
    suspend fun getActorMovieCredit(@Path("person_id") id: Int, @Query("api_key") key: String):
            Response<ActorDetailsMovieCreditModel>

    @GET(SEARCH_MOVIE)
    suspend fun getSearchMovie(@Query("api_key") key: String, @Query("query") search: String):
            Response<SearchMoviesModel>

}