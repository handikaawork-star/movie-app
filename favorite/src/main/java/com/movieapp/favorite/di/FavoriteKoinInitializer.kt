package com.movieapp.favorite.di

import org.koin.core.context.loadKoinModules

object FavoriteKoinInitializer {

    @Volatile
    private var initialized = false

    @Synchronized
    fun init() {
        if (!initialized) {
            loadKoinModules(favoriteModule)
            initialized = true
        }
    }
}
