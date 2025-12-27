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
    val media: List<Any> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val folderName: String? = null,
    val mediaType: String? = null
)

@HiltViewModel
class MediaListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaListUiState())
    val uiState: StateFlow<MediaListUiState> = _uiState.asStateFlow()

    init {
        val folderName = savedStateHandle.get<String>("folderName")
        val mediaType = savedStateHandle.get<String>("mediaType")
        if (folderName != null && mediaType != null) {
            _uiState.value = _uiState.value.copy(folderName = folderName, mediaType = mediaType)
            loadMedia(folderName, mediaType)
        }
    }

    private fun loadMedia(folderName: String, mediaType: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val mediaFlow: Flow<List<Any>> = when (mediaType) {
            "audio" -> mediaRepository.getAudio().map { list -> list.filter { it.folderName == folderName } }
            "video" -> mediaRepository.getVideo().map { list -> list.filter { it.folderName == folderName } }
            else -> flowOf(emptyList())
        }

        mediaFlow.onEach { media ->
            _uiState.value = _uiState.value.copy(media = media, isLoading = false)
        }.catch { e ->
            _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
        }.launchIn(viewModelScope)
    }
}