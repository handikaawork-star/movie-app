package com.movieapp

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat
import com.movieapp.core.di.coreModules
import com.movieapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(coreModules + appModule)
        }
    }
}
