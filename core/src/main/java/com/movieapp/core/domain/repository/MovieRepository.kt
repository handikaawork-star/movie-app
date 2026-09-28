package com.movieapp.core.domain.repository

import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.core.domain.model.MovieDetail
import com.movieapp.core.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(category: MovieCategory): Flow<Resource<List<Movie>>>

    fun getMovieDetail(movieId: Int): Flow<Resource<MovieDetail>>

    fun searchMovies(query: String): Flow<Resource<List<Movie>>>

    fun getFavoriteMovies(): Flow<List<Movie>>

    fun isFavorite(movieId: Int): Flow<Boolean>

    suspend fun setFavorite(movie: Movie, isFavorite: Boolean)
}
