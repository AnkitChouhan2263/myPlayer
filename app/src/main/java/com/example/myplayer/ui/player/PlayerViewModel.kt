package com.example.myplayer.ui.player

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video
import com.example.myplayer.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val playlist: List<Any> = emptyList(),
    val currentMediaIndex: Int = -1,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val totalDuration: Long = 0,
    val currentMediaItem: MediaItem? = null,
    val mediaType: String? = null
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository,
    val player: ExoPlayer // Keep public for PlayerView
) : ViewModel(), Player.Listener {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    val actions = PlayerActions(this)

    private val progressFlow = flow {
        while (true) {
            if (player.isPlaying) {
                emit(player.currentPosition)
            }
            delay(1000)
        }
    }

    init {
        player.addListener(this)
        viewModelScope.launch {
            val mediaType = savedStateHandle.get<String>("mediaType")
            val folderName = savedStateHandle.get<String>("folderName")
            val startIndex = savedStateHandle.get<Int>("startIndex")
            
            _uiState.value = _uiState.value.copy(mediaType = mediaType)

            if (mediaType != null && folderName != null && startIndex != null) {
                val mediaFlow: Flow<List<Any>> = when (mediaType) {
                    "audio" -> mediaRepository.getAudio().map { list -> list.filter { it.folderName == folderName } }
                    "video" -> mediaRepository.getVideo().map { list -> list.filter { it.folderName == folderName } }
                    else -> flowOf(emptyList())
                }

                mediaFlow.collectLatest {
                    _uiState.value = _uiState.value.copy(playlist = it)

                    val clickedMediaUri = when (val media = it.getOrNull(startIndex)) {
                        is Audio -> media.uri
                        is Video -> media.uri
                        else -> null
                    }

                    // If the clicked song is the same as the one already playing, do nothing.
                    if (player.currentMediaItem?.mediaId == clickedMediaUri) {
                        return@collectLatest
                    }

                    prepareAndPlay(it, startIndex)
                }
            }

            progressFlow.collect { position ->
                _uiState.value = _uiState.value.copy(currentPosition = position)
            }
        }
    }

    private fun prepareAndPlay(playlist: List<Any>, startIndex: Int) {
        if (playlist.isEmpty()) return

        player.stop()
        player.clearMediaItems()
        _uiState.value = _uiState.value.copy(currentPosition = 0, totalDuration = 0)

        val mediaItems = playlist.map { media ->
            val uri: Uri
            val metadata: MediaMetadata

            when (media) {
                is Audio -> {
                    uri = Uri.parse(media.uri)
                    metadata = MediaMetadata.Builder()
                        .setTitle(media.displayName)
                        .setArtist(media.artist)
                        .setExtras(Bundle().apply { putString("mediaType", "audio") })
                        .build()
                }
                is Video -> {
                    uri = Uri.parse(media.uri)
                    metadata = MediaMetadata.Builder()
                        .setTitle(media.displayName)
                        .setExtras(Bundle().apply { putString("mediaType", "video") })
                        .build()
                }
                else -> return@map MediaItem.EMPTY
            }
            MediaItem.Builder().setUri(uri).setMediaMetadata(metadata).build()
        }

        player.setMediaItems(mediaItems, startIndex, 0)
        player.prepare()
        player.play()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        _uiState.value = _uiState.value.copy(
            currentMediaIndex = player.currentMediaItemIndex,
            currentMediaItem = mediaItem
        )
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            _uiState.value = _uiState.value.copy(totalDuration = player.duration)
        }
    }

    fun onNext() {
        if (player.hasNextMediaItem()) {
            player.seekToNextMediaItem()
        }
    }

    fun onPrevious() {
        if (player.hasPreviousMediaItem()) {
            player.seekToPreviousMediaItem()
        }
    }

    fun onPlayPause() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun onPageChanged(index: Int) {
        if (uiState.value.currentMediaIndex != index) {
            player.seekTo(index, 0)
        }
    }

    fun onSeek(position: Long) {
        player.seekTo(position)
    }

    override fun onCleared() {
        super.onCleared()
        player.removeListener(this)
    }
}

class PlayerActions(private val viewModel: PlayerViewModel) {
    fun onNext() = viewModel.onNext()
    fun onPrevious() = viewModel.onPrevious()
    fun onPlayPause() = viewModel.onPlayPause()
    fun onPageChanged(index: Int) = viewModel.onPageChanged(index)
    fun onSeek(position: Long) = viewModel.onSeek(position)
}