package com.example.myplayer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : BottomNavItem(
        route = "home",
        icon = Icons.Filled.Home,
        title = "Home"
    )

    object Music : BottomNavItem(
        route = "music",
        icon = Icons.Filled.MusicNote,
        title = "Music"
    )

    object Videos : BottomNavItem(
        route = "videos",
        icon = Icons.Filled.Videocam,
        title = "Videos"
    )

    object Settings : BottomNavItem(
        route = "settings",
        icon = Icons.Filled.Settings,
        title = "Settings"
    )
}