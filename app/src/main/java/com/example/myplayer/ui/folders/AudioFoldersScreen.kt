package com.example.myplayer.ui.folders

import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController

@Composable
fun AudioFoldersScreen(
    navController: NavController,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFolders()
    }

    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else if (uiState.error != null) {
        Text(text = "Error: ${uiState.error}")
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.audioFolders) { folder ->
                Column(modifier = Modifier.clickable { 
                    navController.navigate("media_list/${folder.name}/audio") 
                }) {
                    Text(text = folder.name)
                    Text(text = "${folder.media.size} songs")
                }
            }
        }
    }
}