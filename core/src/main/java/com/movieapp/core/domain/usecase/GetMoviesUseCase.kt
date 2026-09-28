package com.movieapp.core.domain.usecase

import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(category: MovieCategory): Flow<Resource<List<Movie>>> =
        repository.getMovies(category)
}
