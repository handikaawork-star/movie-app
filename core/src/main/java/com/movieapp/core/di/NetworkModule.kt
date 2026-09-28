package com.movieapp.core.di

import com.movieapp.core.BuildConfig
import com.movieapp.core.data.remote.api.TmdbApiService
import com.movieapp.core.data.remote.interceptor.ApiKeyInterceptor
import com.squareup.moshi.Moshi
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val tmdbCertificatePinner = CertificatePinner.Builder()
    .add(
        "api.themoviedb.org",
        "sha256/QfyoR20v8hyYX7L+ikLzM/euPGSDl67gFFcor/sROMs=",
        "sha256/G9LNNAql897egYsabashkzUCTEJkWBzgoEtk8X/678c=",
        "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
    )
    .build()

val networkModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single { ApiKeyInterceptor(BuildConfig.TMDB_API_KEY) }

    single {
        OkHttpClient.Builder()
            .certificatePinner(tmdbCertificatePinner)
            .addInterceptor(get<ApiKeyInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single { Moshi.Builder().build() }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single { get<Retrofit>().create(TmdbApiService::class.java) }
}
