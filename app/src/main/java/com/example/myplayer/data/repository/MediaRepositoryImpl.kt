package com.example.myplayer.data.repository

import com.example.myplayer.data.local.source.MediaStoreSource
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video
import com.example.myplayer.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaStoreSource: MediaStoreSource
) : MediaRepository {

    override fun getAudio(): Flow<List<Audio>> = flow {
        emit(mediaStoreSource.getAudio())
    }

    override fun getVideo(): Flow<List<Video>> = flow {
        emit(mediaStoreSource.getVideo())
    }
}