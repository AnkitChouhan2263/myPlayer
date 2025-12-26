package com.example.myplayer.ui.player

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val mediaUri: String? = null
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val player: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.get<String>("mediaUri")?.let {
                _uiState.value = _uiState.value.copy(mediaUri = it)
                playMedia(it)
            }
        }
    }

    private fun playMedia(mediaUri: String) {
        // Stop any previous playback and clear the playlist
        if (player.isPlaying) {
            player.stop()
        }
        player.clearMediaItems()

        val mediaItem = MediaItem.fromUri(Uri.decode(mediaUri))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        // Do not release the player here. The service owns it.
        player.stop()
        player.clearMediaItems()
    }
}