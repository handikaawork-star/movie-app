package com.movieapp.core.di

import com.movieapp.core.data.repository.MovieRepositoryImpl
import com.movieapp.core.domain.repository.MovieRepository
import org.koin.dsl.module

val dataModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
}
