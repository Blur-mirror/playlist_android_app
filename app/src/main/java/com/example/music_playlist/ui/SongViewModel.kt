package com.example.music_playlist.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.music_playlist.model.Song

class SongViewModel : ViewModel() {
    //recently beat Xenoblade Chronicles Definitive Edition, amazing game. The soundtrack is even better
    val songs = mutableStateListOf(
        Song(1,  "You Will Know Our Names", "ACE+",                    "2010"),
        Song(2,  "Colony 9",                "ACE+",                    "2010"),
        Song(3,  "Mechanical Rhythm",       "ACE+",                    "2021"),
        Song(4,  "Engage The Enemy",        "ACE+, Kenji Hiramatsu",   "2010"),
        Song(5,  "Bionis' Shoulder",        "ACE+ (TOMOri, CHICO)",    "2020")
    )

    // ID counter: always one ahead of the current maximum, safe against deletions
    private var nextId = 6

    // Removes a song by ID. The View calls this, it never receives a callback.
    fun deleteSong(id: Int) {
        songs.removeAll { it.id == id }
    }

    // Validates and appends a new song. Returns false if any field is invalid.
    fun addSong(title: String, artist: String, year: String): Boolean {
        if (title.isBlank() || artist.isBlank() || year.isBlank()) return false
        // Year must be a plausible 4-digit number
        val y = year.trim().toIntOrNull() ?: return false
        if (y < 1900 || y > 2100) return false
        songs.add(Song(nextId++, title.trim(), artist.trim(), year.trim()))
        return true
    }

}