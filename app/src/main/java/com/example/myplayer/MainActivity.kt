package com.example.myplayer

import android.app.PictureInPictureParams
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myplayer.media.MusicServiceConnection
import com.example.myplayer.navigation.AppNavigation
import com.example.myplayer.ui.theme.MyPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPlayerTheme {
                AppNavigation(musicServiceConnection)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val player = musicServiceConnection.mediaController
        if (player != null && player.isPlaying) {
            val aspectRatio = Rational(16, 9)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        musicServiceConnection.release()
    }
}