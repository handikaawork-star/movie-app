package com.movieapp.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.core.domain.model.MovieDetail
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.usecase.GetMovieDetailUseCase
import com.movieapp.core.domain.usecase.IsFavoriteUseCase
import com.movieapp.core.domain.usecase.SetFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    isFavoriteUseCase: IsFavoriteUseCase,
    private val setFavoriteUseCase: SetFavoriteUseCase,
    private val movieId: Int
) : ViewModel() {

    private val _detailState = MutableStateFlow<Resource<MovieDetail>>(Resource.Loading)
    val detailState: StateFlow<Resource<MovieDetail>> = _detailState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = isFavoriteUseCase(movieId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            getMovieDetailUseCase(movieId).collect { _detailState.value = it }
        }
    }

    fun toggleFavorite() {
        val detail = (_detailState.value as? Resource.Success)?.data ?: return
        viewModelScope.launch {
            setFavoriteUseCase(detail.toMovie(), !isFavorite.value)
        }
    }
}
