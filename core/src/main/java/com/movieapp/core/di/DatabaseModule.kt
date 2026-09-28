package com.movieapp.core.di

import androidx.room.Room
import com.movieapp.core.data.local.MovieDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), MovieDatabase::class.java, "movie.db").build()
    }

    single { get<MovieDatabase>().favoriteMovieDao() }
}
