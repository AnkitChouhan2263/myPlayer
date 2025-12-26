package com.example.myplayer.domain.model

data class Folder<T>(
    val name: String,
    val media: List<T>
)