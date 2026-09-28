package com.movieapp.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val category: MovieCategory
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading)
    val uiState: StateFlow<Resource<List<Movie>>> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            getMoviesUseCase(category).collect { _uiState.value = it }
        }
    }
}
