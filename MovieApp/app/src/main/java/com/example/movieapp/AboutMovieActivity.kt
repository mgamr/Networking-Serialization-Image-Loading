package com.example.movieapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.movieapp.networking.AboutMovieEntity
import com.example.movieapp.networking.Dependencies
import com.example.movieapp.networking.MovieApiResponseEntity
import com.example.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.launch


class AboutMovieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val movieId = intent.extras?.getString("movie_id") ?: ""
                    var state by remember {
                        mutableStateOf<SecondScreenState>(SecondScreenState.Loading)
                    }
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(movieId) {
                        coroutineScope.launch {
                            runCatching {
                                Dependencies.apiService.searchMovieById(movieId)
                            }.onSuccess { apiResponse ->
                                state = SecondScreenState.Content(movie = apiResponse)
                            }.onFailure {
                                TODO()
                            }
                        }
                    }

                }
            }
        }
    }
}


sealed interface SecondScreenState {
    data object Loading : SecondScreenState
    data class Content(val movie: AboutMovieEntity) : SecondScreenState
}

@Composable
fun SecondScreen(state: SecondScreenState, context: Context) {
    when (state) {
        is SecondScreenState.Content -> SecondScreenContent(movie = state.movie)
        SecondScreenState.Loading -> ScreenLoading()
    }
}

@Composable
fun SecondScreenContent(movie: AboutMovieEntity) {
    val scroll = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scroll)) {
        Text(text = "This is the new activity")
        Text(text = movie.Title)
        Text(text = movie.Director)
        Text(text = movie.Actors)
        Text(text = movie.BoxOffice)
        Text(text = movie.Director)
        Text(text = movie.Awards)
    }
}
