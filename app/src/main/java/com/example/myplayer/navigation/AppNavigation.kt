package com.example.myplayer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myplayer.ui.home.HomeScreen
import com.example.myplayer.ui.player.PlayerScreen

object Routes {
    const val HOME = "home"
    const val PLAYER = "player"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen()
        }
        composable(Routes.PLAYER) {
            PlayerScreen()
        }
    }
}