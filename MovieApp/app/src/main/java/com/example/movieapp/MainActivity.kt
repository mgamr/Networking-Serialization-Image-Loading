package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.movieapp.networking.Dependencies
import com.example.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    var state by remember {
                        mutableStateOf<MainScreenState>(MainScreenState.Empty)
                    }
                    val coroutineScope = rememberCoroutineScope()
                    MainScreen(
                        state,
                        onLoadMoviesClicked = {
                            state = MainScreenState.Loading
                            coroutineScope.launch {
                                runCatching {
                                    Dependencies.apiService.searchMoviesByTitle(movieName)
                                }.onSuccess { apiResponse ->
                                    state = MainScreenState.Content(movies = apiResponse)
                                }.onFailure {
                                    TODO()
                                }
                            }
                        },
                        this@MainActivity
                    )
                }
            }
        }
    }
}





