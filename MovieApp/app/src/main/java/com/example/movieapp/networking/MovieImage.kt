package com.example.movieapp.networking

import kotlinx.serialization.Serializable

@Serializable
data class MovieImage(
    val imdbID: String,
    val Poster: String
)