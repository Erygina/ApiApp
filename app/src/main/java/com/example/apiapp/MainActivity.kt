package com.example.apiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainActivityViewModel = getViewModel()
        setContent {
            val scope = rememberCoroutineScope()
            val state by viewModel.movieScreenUiState.collectAsState()
            MovieContent(
                genre = remember(state.genre) { state.genre },
                onGenreChange = remember {
                    {
                        viewModel.onGenreChange(it)
                    }
                },
                number = remember(state.number) { state.number },
                onNumberChanged = remember {
                    { newNumber ->
                        viewModel.onNumberChanged(newNumber)
                    }
                },
                onGetMoviesClicked = remember {
                    { genre, number, startDate, endDate ->
                        scope.launch {
                            viewModel.getMovies(genre, number, startDate, endDate)
                        }
                    }
                },
                movies = remember(state.movies) { state.movies },
                startDate = remember(state.startDateTime) { state.startDateTime },
                onStartDateChanged = remember {
                    { newDate ->
                        viewModel.onStartDateChanged(newDate)
                    }
                },
                endDate = remember(state.endDate) { state.endDate },
                onEndDateChanged = remember {
                    { newDate ->
                        viewModel.onEndDateChanged(newDate)
                    }
                },
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieContent(
    genre: String?,
    onGenreChange: (String) -> Unit,

    number: Int?,
    onNumberChanged: (String) -> Unit,

    startDate: Int?,
    onStartDateChanged: (String) -> Unit,

    endDate: Int?,
    onEndDateChanged: (String) -> Unit,

    onGetMoviesClicked: (String?, Int?, Int?, Int?) -> Unit,
    movies: List<Movie>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(3f),
                label = { Text("Жанр") },
                value = genre ?: "",
                onValueChange = onGenreChange,
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                label = { Text("Кол-во") },
                value = number?.toString() ?: "",
                onValueChange = onNumberChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.size(8.dp))

        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                label = { Text("от") },
                value = startDate?.toString() ?: "",
                onValueChange = onStartDateChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                label = { Text("до") },
                value = endDate?.toString() ?: "",
                onValueChange = onEndDateChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = {
                onGetMoviesClicked(genre, number, startDate, endDate)
            }
        ) {
            Text("Найти фильмы")
        }
        Spacer(modifier = Modifier.height(20.dp))
        MovieList(movies)
    }
}

@Composable
fun MovieList(movies: List<Movie>) {
    LazyColumn {
        items(movies.size) { index ->
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = movies[index].photoUrl),
                    contentDescription = "Постер фильма",
                    modifier = Modifier.size(128.dp)
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Название:")
                    Text(text = movies[index].title)
                    Text("Год выхода: ${movies[index].releaseYear}")
                }
            }
            Divider()
        }
    }
}

@Preview
@Composable
fun MovieContentPreview() {
    MovieContent(
        genre = "",
        onGenreChange = {},
        number = 1,
        onNumberChanged = {},
        onGetMoviesClicked = {g, n, s, e ->},
        movies = emptyList(),
        startDate = null,
        endDate = null,
        onStartDateChanged = {},
        onEndDateChanged = {}
    )
}