package com.example.music_playlist.model

// Model class (represents a Song object in the app)
data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val year: String,
    val imageRes: Int,
    val description: String,
    val isFavorite: Boolean = false
)