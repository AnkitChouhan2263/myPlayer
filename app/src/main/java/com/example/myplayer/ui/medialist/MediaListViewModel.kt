package com.example.myplayer.ui.medialist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video
import com.example.myplayer.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class MediaListUiState(
    val audio: List<Audio> = emptyList(),
    val video: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val folderName: String? = null
)

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaListUiState())
    val uiState: StateFlow<MediaListUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("folderName")?.let {
            _uiState.value = _uiState.value.copy(folderName = it)
            loadMedia(it)
        }
    }

    private fun loadMedia(folderName: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val audioFlow = mediaRepository.getAudio().map { it.filter { audio -> audio.folderName == folderName } }
        val videoFlow = mediaRepository.getVideo().map { it.filter { video -> video.folderName == folderName } }

        combine(audioFlow, videoFlow) { audio, video ->
            _uiState.value = _uiState.value.copy(audio = audio, video = video, isLoading = false)
        }.catch { e ->
            _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
        }.launchIn(viewModelScope)
    }
}