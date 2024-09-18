package com.example.movieapp

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.movieapp.networking.AboutMovieEntity
import com.example.movieapp.networking.Dependencies
import com.example.movieapp.networking.MovieApiResponseEntity
import com.example.movieapp.networking.MovieEntity
import com.example.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import coil.request.ImageRequest

var movieName: String by mutableStateOf("")

sealed interface MainScreenState {
    data object Loading : MainScreenState
    data object Empty : MainScreenState
    data class Content(val movies: MovieApiResponseEntity) : MainScreenState
}

@Composable
fun MainScreen(state: MainScreenState, onLoadMoviesClicked: () -> Unit, context: Context) {
    when (state) {
        is MainScreenState.Content -> MainScreenContent(movies = state.movies, context)
        MainScreenState.Empty -> MainScreenEmpty(onLoadMoviesClicked = onLoadMoviesClicked)
        MainScreenState.Loading -> ScreenLoading()
    }
}


@Composable
fun MainScreenContent(movies: MovieApiResponseEntity,  context: Context) {
    val scroll = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scroll)) {
        Text(
            text = movieName,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        movies.Search.forEach {
            MovieListItem(movie = it, context = context) {
//                navigateToSecondScreen(navController, it.imdbID)
            }
        }
    }
}



@Composable
fun MovieListItem(movie: MovieEntity, context: Context, navigateToSecondScreen: () -> Unit) {
    var state by remember {
        mutableStateOf<MainScreenState>(MainScreenState.Empty)
    }
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
//                navigateToSecondScreen()
//
//                var state2 by remember {
//                    mutableStateOf<SecondScreenState>(SecondScreenState.Empty)
//                }
//                val coroutineScope = rememberCoroutineScope()
//                SecondScreen(
//                    state2,
//                    onCardClicked = {
//                        state = SecondScreenState.Loading
//                        coroutineScope.launch {
//                            runCatching {
//                                Dependencies.apiService.searchMovieById(movie.imdbID)
//                            }.onSuccess { apiResponse ->
//                                state = SecondScreenState.Content(movie = apiResponse)
//                            }.onFailure {
//                                TODO()
//                            }
//                        }
//                    }
//                )

//                coroutineScope.launch {
//                    runCatching {
//                        Dependencies.apiService.searchMovieById(movie.imdbID)
//                    }.onSuccess { apiResponse ->
//                        state = MainScreenState.AboutMovie(movie = apiResponse)
//                    }.onFailure {
//                        TODO()
//                    }
//                }
                val intent = Intent(context, AboutMovieActivity::class.java)
                intent.putExtra("movie_id", movie.imdbID)
                context.startActivity(intent)
            }
    ) {
        Column {
            AsyncImage(
                model = coil.request.ImageRequest.Builder(LocalContext.current)
                    .data(movie.Poster)
                    .crossfade(true)
                    .build(),
//                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
            )
//            Image(painter = , contentDescription = null)
            Row (
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = movie.Title,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = movie.Year,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun ScreenLoading() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            CircularProgressIndicator()
            Text(text = "Loading...")
        }
    }
}

@Composable
fun MainScreenEmpty(onLoadMoviesClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = movieName,
            onValueChange = { movieName = it },
            label = { Text("Movie Name") },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
        )
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = { onLoadMoviesClicked() }) {
            Text(text = "Load Movies")
        }
    }
}

