package com.example.music_playlist.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.music_playlist.model.Song
import com.example.music_playlist.R

class SongViewModel : ViewModel() {
    //recently beat Xenoblade Chronicles Definitive Edition, amazing game. The soundtrack is even better
    val songs = mutableStateListOf(
        Song(1, "You Will Know Our Names", "ACE+", "2010", R.drawable.xenoblade_ost,"Music that plays when fighting Unique Monsters"),
        Song(2, "Colony 9", "ACE+", "2010", R.drawable.xenoblade_ost,"Colony 9 background music that plays while you are inside the town."),
        Song(3, "Mechanical Rhythm", "ACE+", "2021", R.drawable.xenoblade_ost,"Battle music that plays when you reach Mechonis"),
        Song(4, "Engage The Enemy", "ACE+, Kenji Hiramatsu", "2010", R.drawable.xenoblade_ost, "Emotional music that plays during key moments of the narrative"),
        Song(5, "Bionis' Shoulder", "ACE+ (TOMOri, CHICO)", "2020", R.drawable.xenoblade_ost,"Background music of the Bionis' shoulder from the Future Connected Epilogue")
    )

    // ID counter: always one ahead of the current maximum, safe against deletions
    private var nextId = 6

    // Removes a song by ID. The View calls this, it never receives a callback.
    fun deleteSong(id: Int) {
        songs.removeAll { it.id == id }
    }

    // Validates and appends a new song. Returns false if any field is invalid.
    fun addSong(title: String, artist: String, year: String, description: String): Boolean {
        if (title.isBlank() || artist.isBlank() || year.isBlank() || description.isBlank()) return false
        // Year must be a plausible 4-digit number
        val y = year.trim().toIntOrNull() ?: return false
        if (y < 1900 || y > 2100) return false
        songs.add(
            Song(
                nextId++,
                title.trim(),
                artist.trim(),
                year.trim(),
                R.drawable.xenoblade_ost,
                description.trim()
            )
        )
        return true
    }
    fun toggleFavorite(id: Int) {
        val index = songs.indexOfFirst { it.id == id }
        if (index != -1) {
            val song = songs[index]
            songs[index] = song.copy(isFavorite = !song.isFavorite)
        }
    }

}