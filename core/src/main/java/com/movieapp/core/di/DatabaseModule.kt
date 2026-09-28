package com.movieapp.core.di

import androidx.room.Room
import com.movieapp.core.data.local.MovieDatabase
import com.movieapp.core.data.local.security.DatabasePassphraseProvider
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single { DatabasePassphraseProvider(androidContext()) }

    single {
        val context = androidContext()
        SQLiteDatabase.loadLibs(context)

        val passphrase = get<DatabasePassphraseProvider>().getPassphrase()
        val factory = SupportFactory(passphrase)

        Room.databaseBuilder(context, MovieDatabase::class.java, "movie.db")
            .openHelperFactory(factory)
            .build()
    }

    single { get<MovieDatabase>().favoriteMovieDao() }
}
