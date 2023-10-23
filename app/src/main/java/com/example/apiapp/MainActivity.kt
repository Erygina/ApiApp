package com.example.apiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainActivityViewModel = getViewModel()
        setContent {
            val scope = rememberCoroutineScope()
            val state by viewModel.movieScreenUiState.collectAsState()
            MovieContent(
                filmName = remember(state.filmName) { state.filmName },
                onFilmNameChange = remember {
                    {
                        viewModel.onFilmNameChange(it)
                    }
                },
                genre = remember(state.genre) { state.genre },
                onGenreChange = remember {
                    {
                        viewModel.onGenreChange(it)
                    }
                },
                onGetMoviesClicked = remember {
                    {
                        scope.launch {
                            viewModel.getMovies()
                        }
                    }
                },
                movies = remember(state.movies) { state.movies }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieContent(
    filmName: String?,
    onFilmNameChange: (String) -> Unit,

    genre: String?,
    onGenreChange: (String) -> Unit,

    onGetMoviesClicked: () -> Unit,
    movies: List<Movie>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                label = { Text("Название") },
                value = filmName ?: "",
                onValueChange = onFilmNameChange
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                label = { Text("Жанр") },
                value = genre ?: "",
                onValueChange = onGenreChange
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = { /*TODO*/ }
            ) {
                Text("Найти по названию")
            }
            OutlinedButton(
                onClick = { /*TODO*/ }
            ) {
                Text("Найти по жанру")
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = onGetMoviesClicked
        ) {
            Text("Найти 100 фильмов")
        }
        Spacer(modifier = Modifier.height(20.dp))
        MovieList(movies)
    }
}

@Composable
fun MovieList(movies: List<Movie>) {
    LazyColumn {
        items(movies.size) { index ->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = movies[index].title)
                    Text(text = movies[index].releaseDate)
                    Text(text = movies[index].rating.toString())
                    Text(text = movies[index].overview)
                }
            }
        }
    }
}

@Preview
@Composable
fun MovieContentPreview() {
    MovieContent(
        filmName = "Пират Карибского моря",
        onFilmNameChange = {},
        genre = "",
        onGenreChange = {},
        onGetMoviesClicked = {},
        movies = emptyList()
    )
}