package com.movieapp.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.usecase.GetFavoriteMoviesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {

    val favorites: StateFlow<List<Movie>> = getFavoriteMoviesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
