package com.movieapp.core.data.mapper

import com.movieapp.core.data.local.FavoriteMovieEntity
import com.movieapp.core.data.remote.dto.MovieDetailDto
import com.movieapp.core.data.remote.dto.MovieDto
import com.movieapp.core.domain.model.Movie
import com.movieapp.core.domain.model.MovieDetail

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate.orEmpty(),
    voteAverage = voteAverage ?: 0.0
)

fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    id = id,
    title = title.orEmpty(),
    overview = overview.orEmpty(),
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate.orEmpty(),
    voteAverage = voteAverage ?: 0.0,
    runtime = runtime ?: 0,
    genres = genres.orEmpty().mapNotNull { it.name }
)

fun FavoriteMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage
)

fun Movie.toEntity(): FavoriteMovieEntity = FavoriteMovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage
)
