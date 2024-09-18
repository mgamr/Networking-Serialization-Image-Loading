package com.example.movieapp.networking

import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResponseEntity(
    val Search: List<MovieEntity>,
    val totalResults: String,
    val Response: String
)

@Serializable
data class MovieEntity(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Poster: String,
)

@Serializable
data class AboutMovieEntity(
    val Title: String,
    val Poster: String,
    val Director: String,
    val Actors: String,
    val BoxOffice: String,
    val Awards: String,
)
