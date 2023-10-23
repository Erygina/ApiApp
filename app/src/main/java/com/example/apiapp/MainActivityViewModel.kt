package com.example.apiapp

import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivityViewModel : ViewModel() {

    private val _movieScreenUiState = MutableStateFlow(MovieScreenState())
    val movieScreenUiState: StateFlow<MovieScreenState> = _movieScreenUiState

    fun onFilmNameChange(newName: String) {
        _movieScreenUiState.update { prevState ->
            prevState.copy(
                filmName = newName
            )
        }
    }

    fun onGenreChange(newGenre: String) {
        _movieScreenUiState.update { prevState ->
            prevState.copy(
                genre = newGenre
            )
        }
    }

    fun getMovies() = viewModelScope.launch {
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://moviesdatabase.p.rapidapi.com/titles/series/%7BseriesId%7D")
            .get()
            .addHeader("X-RapidAPI-Key", "0c45071da7msh48a4d445e8be1f8p170517jsnda6e5f21b8e0")
            .addHeader("X-RapidAPI-Host", "moviesdatabase.p.rapidapi.com")
            .build()

//        val response = client.newCall(request).execute()

        val responses: Response = client.newCall(request).execute()
        val jsonData = responses.body.toString()
        val movies = Gson().fromJson(jsonData, Movie::class.java)
        Log.d("movie", "$movies")

    }
}

data class MovieScreenState(
    val filmName: String? = null,
    val genre: String? = null,
    val movies: List<Movie> = emptyList()
)