package com.movieapp.core.domain.usecase

import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class SearchMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(query: String): Flow<Resource<List<Movie>>> =
        repository.searchMovies(query)
}
