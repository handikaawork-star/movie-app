package com.movieapp.core.data.remote.api

import com.movieapp.core.data.remote.dto.MovieDetailDto
import com.movieapp.core.data.remote.dto.MovieListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page: Int = 1): MovieListResponseDto

    @GET("movie/popular")
    suspend fun getPopular(@Query("page") page: Int = 1): MovieListResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") page: Int = 1): MovieListResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): MovieDetailDto

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieListResponseDto
}
