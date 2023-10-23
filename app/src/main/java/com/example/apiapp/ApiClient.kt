package com.example.apiapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://rapidapi.com/SAdrian/api/moviesdatabase/"
    private const val API_KEY = "0c45071da7msh48a4d445e8be1f8p170517jsnda6e5f21b8e0"

    fun create(): MoviesApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .url("https://moviesdatabase.p.rapidapi.com/titles?genre=drama")
                    .addHeader("x-rapidapi-key", API_KEY)
                    .addHeader("x-rapidapi-host", "moviesdatabase.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MoviesApi::class.java)
    }
}