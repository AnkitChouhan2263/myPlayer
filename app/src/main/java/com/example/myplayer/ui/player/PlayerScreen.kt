package com.example.myplayer.ui.player

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video
import com.example.myplayer.utils.toFormattedTimeString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(viewModel: PlayerViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.mediaType) {
        "audio" -> {
            if (uiState.playlist.isNotEmpty() && uiState.currentMediaIndex != -1) {
                val pagerState = rememberPagerState(
                    initialPage = uiState.currentMediaIndex,
                    pageCount = { uiState.playlist.size }
                )

                LaunchedEffect(pagerState.currentPage) {
                    viewModel.actions.onPageChanged(pagerState.currentPage)
                }

                LaunchedEffect(uiState.currentMediaIndex) {
                    if (pagerState.currentPage != uiState.currentMediaIndex) {
                        pagerState.animateScrollToPage(uiState.currentMediaIndex)
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val media = uiState.playlist[page]
                    if (media is Audio) {
                        AudioPlayer(
                            audio = media,
                            uiState = uiState,
                            actions = viewModel.actions
                        )
                    }
                }
            }
        }
        "video" -> {
            val media = uiState.playlist.getOrNull(uiState.currentMediaIndex)
            if (media is Video) {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = viewModel.player
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AudioPlayer(
    audio: Audio,
    uiState: PlayerUiState,
    actions: PlayerActions
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    // Update slider position based on playback state, but only when not being dragged.
    LaunchedEffect(uiState.currentPosition, isDragging) {
        if (!isDragging) {
            sliderPosition = uiState.currentPosition.toFloat()
        }
    }

    val totalDuration = (uiState.totalDuration.takeIf { it > 0 } ?: 0L).toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val albumArtUri = audio.albumId?.let {
            ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), it)
        }

        AsyncImage(
            model = albumArtUri,
            contentDescription = "Album Art",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            error = rememberVectorPainter(Icons.Filled.MusicNote),
            placeholder = rememberVectorPainter(Icons.Filled.MusicNote)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = audio.displayName, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = audio.artist, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { 
                isDragging = true
                sliderPosition = it 
            },
            onValueChangeFinished = {
                isDragging = false
                actions.onSeek(sliderPosition.toLong())
            },
            valueRange = 0f..totalDuration,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = sliderPosition.toLong().toFormattedTimeString())
            Text(text = uiState.totalDuration.toFormattedTimeString())
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = actions::onPrevious) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous")
            }
            IconButton(onClick = actions::onPlayPause, modifier = Modifier.size(72.dp)) {
                Icon(
                    imageVector = if (uiState.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = "Play/Pause",
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(onClick = actions::onNext) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Next")
            }
        }
    }
}