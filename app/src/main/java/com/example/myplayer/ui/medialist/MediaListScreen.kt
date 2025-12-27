package com.example.myplayer.ui.medialist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video

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
                itemsIndexed(uiState.media) { index, media ->
                    val displayName = when (media) {
                        is Audio -> media.displayName
                        is Video -> media.displayName
                        else -> ""
                    }
                    Text(
                        text = displayName,
                        modifier = Modifier.clickable { 
                            navController.navigate("player/${uiState.mediaType}/${uiState.folderName}/$index") 
                        }
                    )
                }
            }
        }
    }
}