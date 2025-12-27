package com.example.myplayer.media

import androidx.media3.common.MediaItem

data class PlaybackState(
    val mediaItem: MediaItem? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val totalDuration: Long = 0
)
