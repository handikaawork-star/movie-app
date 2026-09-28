package com.movieapp.core.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListResponseDto(
    @Json(name = "page") val page: Int,
    @Json(name = "results") val results: List<MovieDto>
)
