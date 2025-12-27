package com.example.myplayer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myplayer.media.MusicServiceConnection

@Composable
fun MiniPlayer(musicServiceConnection: MusicServiceConnection) {
    val controller = musicServiceConnection.mediaController

    if (controller != null && controller.currentMediaItem != null) {
        val mediaMetadata = controller.currentMediaItem?.mediaMetadata

        Column {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album art placeholder
                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = mediaMetadata?.title?.toString() ?: "")
                    Text(text = mediaMetadata?.artist?.toString() ?: "")
                }

                Row {
                    IconButton(onClick = { controller.seekToPrevious() }) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Previous")
                    }
                    IconButton(onClick = { if (controller.isPlaying) controller.pause() else controller.play() }) {
                        Icon(
                            imageVector = if (controller.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause"
                        )
                    }
                    IconButton(onClick = { controller.seekToNext() }) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next")
                    }
                }
            }
        }
    }
}