package com.example.myplayer.domain.model

data class Video(
    val id: Long,
    val uri: String,
    val displayName: String,
    val duration: Int,
    val size: Int,
    val folderName: String
)