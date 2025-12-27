package com.example.myplayer.media

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicServiceConnection @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var progressJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState = _playbackState.asStateFlow()

    val mediaController: MediaController? get() = if (mediaControllerFuture?.isDone == true) mediaControllerFuture?.get() else null

    init {
        val sessionToken = SessionToken(context, ComponentName(context, MediaService::class.java))
        mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        mediaControllerFuture?.addListener({ setupController() }, MoreExecutors.directExecutor())
    }

    fun release() {
        mediaControllerFuture?.let { MediaController.releaseFuture(it) }
    }

    private fun setupController() {
        val controller = mediaController ?: return
        controller.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _playbackState.value = _playbackState.value.copy(mediaItem = mediaItem)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                updateProgress(isPlaying)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _playbackState.value = _playbackState.value.copy(totalDuration = controller.duration)
                }
            }
        })
    }

    private fun updateProgress(isPlaying: Boolean) {
        progressJob?.cancel()
        if (isPlaying) {
            progressJob = serviceScope.launch {
                while (true) {
                    _playbackState.value = _playbackState.value.copy(currentPosition = mediaController?.currentPosition ?: 0)
                    delay(1000)
                }
            }
        }
    }
}