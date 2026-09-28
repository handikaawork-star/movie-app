package com.movieapp.core.domain.usecase

import com.movieapp.core.domain.model.MovieDetail
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMovieDetailUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<Resource<MovieDetail>> =
        repository.getMovieDetail(movieId)
}
