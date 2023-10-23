package com.example.apiapp

import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("movies/all")
    suspend fun getMovies(): List<Movie>
}