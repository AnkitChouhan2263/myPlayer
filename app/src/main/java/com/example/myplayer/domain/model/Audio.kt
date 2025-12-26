package com.example.myplayer.domain.model

data class Audio(
    val id: Long,
    val uri: String,
    val displayName: String,
    val artist: String,
    val duration: Int,
    val size: Int,
    val albumId: Long?
)