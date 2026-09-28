package com.movieapp.core.domain.usecase

import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> = repository.getFavoriteMovies()
}
