package com.movieapp.di

import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.detail.MovieDetailViewModel
import com.movieapp.movies.MovieListViewModel
import com.movieapp.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { (category: MovieCategory) -> MovieListViewModel(get(), category) }

    viewModel { (movieId: Int) -> MovieDetailViewModel(get(), get(), get(), movieId) }

    viewModel { SearchViewModel(get()) }
}
