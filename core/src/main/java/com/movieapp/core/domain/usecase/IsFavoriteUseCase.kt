package com.movieapp.core.domain.usecase

import com.movieapp.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class IsFavoriteUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<Boolean> = repository.isFavorite(movieId)
}
