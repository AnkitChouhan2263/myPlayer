package com.example.myplayer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Video
import com.example.myplayer.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class HomeUiState(
    val audio: List<Audio> = emptyList(),
    val video: List<Video> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadMedia() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        mediaRepository.getAudio()
            .onEach { audio ->
                _uiState.value = _uiState.value.copy(audio = audio, isLoading = false)
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
            .launchIn(viewModelScope)

        mediaRepository.getVideo()
            .onEach { video ->
                _uiState.value = _uiState.value.copy(video = video, isLoading = false)
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
            .launchIn(viewModelScope)
    }
}