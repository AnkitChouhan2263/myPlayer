package com.example.myplayer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myplayer.ui.components.BottomNavigationBar
import com.example.myplayer.ui.folders.AudioFoldersScreen
import com.example.myplayer.ui.folders.VideoFoldersScreen
import com.example.myplayer.ui.home.HomeScreen
import com.example.myplayer.ui.medialist.MediaListScreen
import com.example.myplayer.ui.player.PlayerScreen
import com.example.myplayer.ui.settings.SettingsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Music,
                    BottomNavItem.Videos,
                    BottomNavItem.Settings
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController)
            }
            composable(BottomNavItem.Music.route) {
                AudioFoldersScreen(navController)
            }
            composable(BottomNavItem.Videos.route) {
                VideoFoldersScreen(navController)
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
            composable("media_list/{folderName}") {
                MediaListScreen(navController)
            }
            composable("player/{mediaUri}") {
                PlayerScreen()
            }
        }
    }
}