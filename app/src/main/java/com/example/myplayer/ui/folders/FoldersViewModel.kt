package com.example.myplayer.ui.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplayer.domain.model.Audio
import com.example.myplayer.domain.model.Folder
import com.example.myplayer.domain.model.Video
import com.example.myplayer.domain.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

data class FoldersUiState(
    val audioFolders: List<Folder<Audio>> = emptyList(),
    val videoFolders: List<Folder<Video>> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoldersUiState())
    val uiState: StateFlow<FoldersUiState> = _uiState.asStateFlow()

    fun loadFolders() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        val audioFoldersFlow = mediaRepository.getAudioFolders()
        val videoFoldersFlow = mediaRepository.getVideoFolders()

        combine(audioFoldersFlow, videoFoldersFlow) { audioFolders, videoFolders ->
            _uiState.value = FoldersUiState(
                audioFolders = audioFolders,
                videoFolders = videoFolders,
                isLoading = false
            )
        }.catch { e ->
            _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
        }.launchIn(viewModelScope)
    }
}