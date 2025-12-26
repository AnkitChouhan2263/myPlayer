package com.example.myplayer.ui.medialist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MediaListScreen(
    navController: NavController,
    viewModel: MediaListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else if (uiState.error != null) {
        Text(text = "Error: ${uiState.error}")
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Files in ${uiState.folderName}")
            LazyColumn {
                items(uiState.audio) { audio ->
                    Text(
                        text = audio.displayName,
                        modifier = Modifier.clickable { 
                            val encodedUri = URLEncoder.encode(audio.uri, StandardCharsets.UTF_8.toString())
                            navController.navigate("player/$encodedUri") 
                        }
                    )
                }
                items(uiState.video) { video ->
                    Text(
                        text = video.displayName,
                        modifier = Modifier.clickable { 
                            val encodedUri = URLEncoder.encode(video.uri, StandardCharsets.UTF_8.toString())
                            navController.navigate("player/$encodedUri") 
                        }
                    )
                }
            }
        }
    }
}