package com.example.myplayer.ui.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView

@Composable
fun PlayerScreen(viewModel: PlayerViewModel = hiltViewModel()) {

    // DisposableEffect ensures that the player is stopped when the screen is disposed.
    DisposableEffect(Unit) {
        onDispose {
            viewModel.player.stop()
        }
    }

    // AndroidView is the correct way to embed a classic Android View in Compose.
    // The factory block is called only once to create the view, preventing memory leaks.
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = viewModel.player
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}