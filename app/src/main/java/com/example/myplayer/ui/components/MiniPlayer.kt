package com.example.myplayer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myplayer.media.MusicServiceConnection

@Composable
fun MiniPlayer(musicServiceConnection: MusicServiceConnection) {
    val playbackState by musicServiceConnection.playbackState.collectAsState()
    val controller = musicServiceConnection.mediaController

    if (playbackState.mediaItem == null) return

    val mediaMetadata = playbackState.mediaItem?.mediaMetadata
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    LaunchedEffect(playbackState.currentPosition, isDragging) {
        if (!isDragging) {
            sliderPosition = playbackState.currentPosition.toFloat()
        }
    }

    val totalDuration = (playbackState.totalDuration.takeIf { it > 0 } ?: 0L).toFloat()

    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { 
                isDragging = true
                sliderPosition = it 
            },
            onValueChangeFinished = {
                isDragging = false
                controller?.seekTo(sliderPosition.toLong())
            },
            valueRange = 0f..totalDuration,
            modifier = Modifier.fillMaxWidth().height(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.size(48.dp))

            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(text = mediaMetadata?.title?.toString() ?: "", maxLines = 1)
                Text(text = mediaMetadata?.artist?.toString() ?: "", maxLines = 1)
            }

            Row {
                IconButton(onClick = { controller?.seekToPrevious() }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                }
                IconButton(onClick = { if (playbackState.isPlaying) controller?.pause() else controller?.play() }) {
                    Icon(
                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause"
                    )
                }
                IconButton(onClick = { controller?.seekToNext() }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next")
                }
            }
        }
    }
}