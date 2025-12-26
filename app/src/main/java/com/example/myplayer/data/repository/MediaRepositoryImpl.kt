package com.example.myplayer.data.repository

import com.example.myplayer.data.local.source.MediaStoreSource
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Folder
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

    override fun getAudioFolders(): Flow<List<Folder<Audio>>> = flow {
        val audio = mediaStoreSource.getAudio()
        val folders = audio.groupBy { it.folderName }.map { (name, media) ->
            Folder(name, media)
        }
        emit(folders)
    }

    override fun getVideoFolders(): Flow<List<Folder<Video>>> = flow {
        val video = mediaStoreSource.getVideo()
        val folders = video.groupBy { it.folderName }.map { (name, media) ->
            Folder(name, media)
        }
        emit(folders)
    }
}