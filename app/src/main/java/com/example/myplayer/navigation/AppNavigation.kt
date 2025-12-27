package com.example.myplayer.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myplayer.media.MusicServiceConnection
import com.example.myplayer.ui.components.BottomNavigationBar
import com.example.myplayer.ui.components.MiniPlayer
import com.example.myplayer.ui.folders.AudioFoldersScreen
import com.example.myplayer.ui.folders.VideoFoldersScreen
import com.example.myplayer.ui.home.HomeScreen
import com.example.myplayer.ui.medialist.MediaListScreen
import com.example.myplayer.ui.player.PlayerScreen
import com.example.myplayer.ui.settings.SettingsScreen

@Composable
fun AppNavigation(musicServiceConnection: MusicServiceConnection) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "player/{mediaType}/{folderName}/{startIndex}") {
                Column {
                    MiniPlayer(musicServiceConnection)
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
            }
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
            composable(
                route = "media_list/{folderName}/{mediaType}",
                arguments = listOf(
                    navArgument("folderName") { type = NavType.StringType },
                    navArgument("mediaType") { type = NavType.StringType }
                )
            ) {
                MediaListScreen(navController)
            }
            composable(
                route = "player/{mediaType}/{folderName}/{startIndex}",
                arguments = listOf(
                    navArgument("mediaType") { type = NavType.StringType },
                    navArgument("folderName") { type = NavType.StringType },
                    navArgument("startIndex") { type = NavType.IntType }
                )
            ) {
                PlayerScreen()
            }
        }
    }
}