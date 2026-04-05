package com.example.music_playlist.ui
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import android.R.attr.description
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote //add a nice icon to it, needs to be added into the build kts dependency.
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.music_playlist.model.Song
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(viewModel: SongViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Xenoblade OST Playlist",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B0036) // nice purple
                )
            )
        },
        containerColor = Color(0xFF0F0020)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // key = song.id: Compose uses this to track identity across recompositions.
            // Without it, deleting song #2 causes songs #3-5 to re-render and any
            // composable-local state bleeds into adjacent items.
            items(
                items = viewModel.songs,
                key = { it.id },
                contentType = { "song_card" } // Helps Compose recycle the UI efficiently
            ) { song ->
                SongCard(
                    song = song,
                    onDelete = { viewModel.deleteSong(song.id) },
                    onDetails = { navController.navigate("details/${song.id}") },
                    onToggleFavorite = { viewModel.toggleFavorite(song.id) },
                    modifier = Modifier.animateItem(
                        // Optional: customize the slide/fade behavior
                        placementSpec = spring(stiffness = Spring.StiffnessLow),
                        fadeInSpec = spring(stiffness = Spring.StiffnessMedium),
                        fadeOutSpec = spring(stiffness = Spring.StiffnessMedium)
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // The add form is the last item in the lazy column so it scrolls
            // naturally below the list rather than being fixed on screen
            item {
                AddSongForm(
                    onAdd = { title, artist, year, description ->
                        viewModel.addSong(title, artist, year, description.toString())
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}


// Single song card.
// Pure View: receives data and a lambda, knows nothing about where
// data comes from or what happens after onDelete is invoked.
@Composable
fun SongCard(
    song: Song,
    onDelete: () -> Unit,
    onDetails: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A0A4A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Music note accent
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF6A0DAD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Song metadata — weight(1f) pushes the delete button to the far right
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = song.artist,
                    fontSize = 13.sp,
                    color = Color(0xFFBB86FC) // Material purple accent
                )
                Text(
                    text = song.year,
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            // Favourite button
            IconButton(onClick = { onToggleFavorite() }) {
                Icon(
                    imageVector = if (song.isFavorite)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (song.isFavorite) Color.Red else Color.Gray
                )
            }

            //Delete button
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${song.title}",
                    tint = Color(0xFFFF5252)
                )
            }

            //Details button
            Button(
                onClick = onDetails,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB86FC))
            ) {
                Text("Details", color = Color.White)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailScreen(song: Song, navController: NavController) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        song.title,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = song.imageRes),
                contentDescription = song.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )


            Text(
                text = "Artist: ${song.artist}",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp
            )

            Text(
                text = "Year: ${song.year}",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp
            )

            Text(
                text = "Description: ${song.description}",
                color = MaterialTheme.colorScheme.onBackground
            )

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Back", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}



// Add-song form.
// Local form state (what the user is currently typing) lives here with
// remember{}.

// Domain state (the actual song list) lives in the ViewModel.
@Composable
fun AddSongForm(onAdd: (String, String, String, String) -> Boolean) {
    var title  by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var year   by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var error  by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0A3C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                "Add a Song",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFBB86FC)
            )

            // Helper lambda so we don't repeat OutlinedTextField styling three times
            @Composable
            fun SongField(value: String, onChange: (String) -> Unit, label: String,
                          numeric: Boolean = false) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { onChange(it); error = "" },
                    label = { Text(label, color = Color(0xFF9E9E9E)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = if (numeric)
                        KeyboardOptions(keyboardType = KeyboardType.Number)
                    else KeyboardOptions.Default,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = Color(0xFFBB86FC),
                        unfocusedBorderColor = Color(0xFF555555),
                        focusedTextColor     = Color.White,
                        unfocusedTextColor   = Color.White
                    )
                )
            }

            SongField(title,  { title  = it }, "Title")
            SongField(artist, { artist = it }, "Artist")
            SongField(year,   { year   = it }, "Year", numeric = true)
            SongField(description, { description = it }, "description")


            // Inline error message, only shown when error string is non-empty
            if (error.isNotEmpty()) {
                Text(error, color = Color(0xFFFF5252), fontSize = 13.sp)
            }

            Button(
                onClick = {
                    val ok = onAdd(title, artist, year, description)
                    if (ok) {
                        title = ""; artist = ""; year = ""; error = ""
                    } else {
                        error = when {
                            title.isBlank()             -> "Title cannot be empty."
                            artist.isBlank()            -> "Artist cannot be empty."
                            year.toIntOrNull() == null  -> "Year must be a number."
                            else                        -> "Year must be between 1900 and 2100."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD))
            ) {
                Text("Add to Playlist", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
