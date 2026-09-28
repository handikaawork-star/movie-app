package com.movieapp.core.domain.usecase

import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.repository.MovieRepository

class SetFavoriteUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movie: Movie, isFavorite: Boolean) =
        repository.setFavorite(movie, isFavorite)
}
