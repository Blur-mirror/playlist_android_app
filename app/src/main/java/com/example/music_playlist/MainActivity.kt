package com.example.music_playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.music_playlist.ui.PlaylistScreen //UI
import com.example.music_playlist.ui.SongViewModel //Mediator between View and Model
import com.example.music_playlist.ui.theme.Music_playlistTheme
import com.example.music_playlist.ui.SongDetailScreen
import androidx.navigation.compose.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*

class MainActivity : ComponentActivity() {

    private val viewModel: SongViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_playlistTheme {
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: SongViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "playlist") {

        composable("playlist") {
            PlaylistScreen(viewModel, navController)
        }

        composable("details/{songId}") { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId")?.toIntOrNull()
            val song = viewModel.songs.find { it.id == songId }

            song?.let {
                SongDetailScreen(song = it, navController = navController)
            }
        }
    }
}