package com.example.apiapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivityViewModel : ViewModel() {

    private val _movieScreenUiState = MutableStateFlow(MovieScreenState())
    val movieScreenUiState: StateFlow<MovieScreenState> = _movieScreenUiState

    fun onGenreChange(newGenre: String) {
        _movieScreenUiState.update { prevState ->
            prevState.copy(
                genre = newGenre
            )
        }
    }

    fun onNumberChanged(newNumber: String) {
        _movieScreenUiState.update { prevState ->
            prevState.copy(
                number = newNumber.toIntOrNull()
            )
        }
    }

    fun onStartDateChanged(newDate: String) {
        _movieScreenUiState.update { prevState ->
            prevState.copy(
                startDateTime = newDate.toIntOrNull()
            )
        }
    }

    fun onEndDateChanged(newDate: String) {
        _movieScreenUiState.update { prevState ->
            prevState.copy(
                endDate = newDate.toIntOrNull()
            )
        }
    }

    fun getMovies(
        genre: String? = null,
        number: Int? = null,
        startDateTime: Int? = null,
        endDate: Int? = null
    ) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()
            Log.d("genre", "$genre")
            var url  = "https://moviesdatabase.p.rapidapi.com/titles?"
            if (!genre.isNullOrEmpty()) {
                url += "genre=$genre"
            }
            if (startDateTime != null) {
                url += "&startYear=$startDateTime"
            }
            url += "&list=most_pop_movies"
            if (endDate != null) {
                url += "&endYear=$endDate"
            }
            if (number != null) {
                url += "&limit=$number"
            }
            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "0c45071da7msh48a4d445e8be1f8p170517jsnda6e5f21b8e0")
                .addHeader("X-RapidAPI-Host", "moviesdatabase.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            val json = JSONObject(responseBody)

            var movieList = mutableListOf<Movie>()
            val results = json.getJSONArray("results")

            for (i in 0 until results.length()){
                val result = results.getJSONObject(i)
                val title = result.getJSONObject("titleText").getString("text")
                val photoUrl = result.getJSONObject("primaryImage").getString("url")
                val releaseYear = result.getJSONObject("releaseYear").getString("year")

                movieList.add(i, Movie(title, photoUrl, releaseYear))
            }
            _movieScreenUiState.update { prevState ->
                prevState.copy(
                    movies = movieList
                )
            }
        }
    }
}

data class MovieScreenState(
    val genre: String? = null,
    val number: Int? = null,
    val startDateTime: Int? = null,
    val endDate: Int? = null,
    val movies: List<Movie> = emptyList(),
)