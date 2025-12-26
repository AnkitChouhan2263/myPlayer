package com.example.myplayer.ui.home

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    val uiState by viewModel.uiState.collectAsState()

    if (permissionsState.allPermissionsGranted) {
        LaunchedEffect(Unit) {
            viewModel.loadMedia()
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.error != null) {
            Text(text = "Error: ${uiState.error}")
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Audio Files")
                LazyColumn {
                    items(uiState.audio) { audio ->
                        Text(text = audio.displayName)
                    }
                }
                Text(text = "Video Files")
                LazyColumn {
                    items(uiState.video) { video ->
                        Text(text = video.displayName)
                    }
                }
            }
        }
    } else {
        Text(text = "Storage permissions are required to play media.")
    }
}