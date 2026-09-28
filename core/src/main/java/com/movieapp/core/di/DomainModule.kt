package com.movieapp.core.di

import com.movieapp.core.domain.usecase.GetFavoriteMoviesUseCase
import com.movieapp.core.domain.usecase.GetMovieDetailUseCase
import com.movieapp.core.domain.usecase.GetMoviesUseCase
import com.movieapp.core.domain.usecase.IsFavoriteUseCase
import com.movieapp.core.domain.usecase.SearchMoviesUseCase
import com.movieapp.core.domain.usecase.SetFavoriteUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetMoviesUseCase(get()) }
    factory { GetMovieDetailUseCase(get()) }
    factory { GetFavoriteMoviesUseCase(get()) }
    factory { IsFavoriteUseCase(get()) }
    factory { SetFavoriteUseCase(get()) }
    factory { SearchMoviesUseCase(get()) }
}
