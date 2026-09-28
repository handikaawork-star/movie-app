package com.movieapp.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val uiState: StateFlow<Resource<List<Movie>>> = _query
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(Resource.Success(emptyList()))
            } else {
                searchMoviesUseCase(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Success(emptyList()))

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }
}
