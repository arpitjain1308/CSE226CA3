package com.example.unit4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieApp : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieScreen()
        }
    }
}

data class Movie(
    val id: Int,
    val name: String,
    val genre: String
)

class MovieRepository {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()
    private val _totalMovies = MutableStateFlow(0)
    val totalMovies: StateFlow<Int> = _totalMovies.asStateFlow()
    fun add(name: String, genre: String) {
        val movie = Movie(
            id = _movies.value.size + 1,
            name = name,
            genre = genre
        )
        _movies.value = _movies.value + movie
        _totalMovies.value = _movies.value.size
    }
}


class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()
    val movies = repository.movies
    val totalMovies = repository.totalMovies
    fun add(name: String, genre: String) {
        repository.add(name, genre)
    }
}

@Composable
fun MovieScreen(
    viewModel: MovieViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    val movies by viewModel.movies.collectAsState()
    val totalMovies by viewModel.totalMovies.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Movie App")
        Text("Total Movies: $totalMovies")
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") }
        )
        Button(
            onClick = {
                viewModel.add(name, genre)
            }
        ) {
            Text("Add Movie")
        }
        LazyColumn {
            items(movies) { movie ->

                Text("Name: ${movie.name}")
                Text("Genre: ${movie.genre}")
            }
        }
    }
}
