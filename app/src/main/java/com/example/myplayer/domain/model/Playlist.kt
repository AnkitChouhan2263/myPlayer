package com.example.myplayer.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val mediaIds: List<Long>
)