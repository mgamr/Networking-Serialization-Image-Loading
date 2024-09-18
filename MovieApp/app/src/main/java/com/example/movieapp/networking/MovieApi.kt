package com.example.movieapp.networking

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

val API_KEY = "c34651c7"
private const val BASE_URL =
    "https://www.omdbapi.com/"

interface MovieApi {
    @GET("/")
    suspend fun searchMoviesByTitle(
        @Query("s") title: String,
        @Query("apikey") apiKey: String = API_KEY
    ): MovieApiResponseEntity

    @GET("/")
    suspend fun searchMovieById(
        @Query("i") id: String,
        @Query("apikey") apiKey: String = API_KEY
    ): AboutMovieEntity
}

object Dependencies {
    val json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=UTF8".toMediaTypeOrNull()!!)
            )
            .build()
    }

    val apiService by lazy {
        retrofit.create<MovieApi>()
    }
}

