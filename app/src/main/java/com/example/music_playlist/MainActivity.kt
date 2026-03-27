package com.example.music_playlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.music_playlist.ui.PlaylistScreen //UI
import com.example.music_playlist.ui.SongViewModel //Mediator between View and Model
import com.example.music_playlist.ui.theme.Music_playlistTheme

class MainActivity : ComponentActivity() {



    private val viewModel: SongViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_playlistTheme {
                PlaylistScreen(viewModel = viewModel)
            }
        }
    }
}