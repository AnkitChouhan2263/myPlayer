package com.example.myplayer.domain.repository

import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getAudio(): Flow<List<Audio>>
    fun getVideo(): Flow<List<Video>>
}